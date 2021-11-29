package com.sweethome.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.SchoolClassDao;
import com.sweethome.entities.*;
import com.sweethome.service.*;
import com.sweethome.utils.DateUtilSweet;
import com.sweethome.vo.StudentClockInVo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchoolClassServiceImpl extends ServiceImpl<SchoolClassDao, ClassEntity> implements SchoolClassService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClockInService clockInService;

    /**
     * 添加班级
     * 如果实体类中传入的 班主任 的信息则需要同时更新对应班主任中的所带班级字段
     * 如果为班主任第一次绑定班级则直接添加 如果班主任的班级字段还绑定其他班级则需要取出字段进行更新，保留之前的班级信息
     *
     * 也需要同时更新专业信息，专业表中有 class_ids 字段，专业下面所有班级id
     * @param classEntity 实体类参数
     */
    @Override
    public void saveSchoolClass(ClassEntity classEntity) {
        boolean save = this.saveOrUpdate(classEntity);
        Long teacherId = classEntity.getTeacherId();
        Long majorId = classEntity.getMajorId();
        ClassEntity cla = this.getClassById(classEntity.getCaId());
        if (save && teacherId != null) {
            TeacherEntity teacher = teacherService.getTeacherById(teacherId);
            if (teacher != null) {
                if (teacher.getCaIds() != null) {
                    String caIds = teacher.getCaIds();
                    List<Long> longs = JSON.parseArray(caIds, Long.class);
                    longs.add(cla.getCaId());
                    teacherService.updateClass(teacherId, ArrayUtils.toString(longs));
                }
            }
            ArrayList<Long> longs = new ArrayList<>();
            longs.add(cla.getCaId());
            teacherService.updateClass(teacherId, longs.toString());
        }
        if (majorId != null) {
            MajorEntity major = majorService.getById(majorId);
            if (major != null){
                String classIds = major.getClassIds();
                if (classIds != null) {
                    List<Long> longs = JSON.parseArray(classIds, Long.class);
                    longs.add(cla.getCaId());
                    majorService.updateClass(major.getMaId(), ArrayUtils.toString(longs));
                } else {
                    ArrayList<Long> claas = new ArrayList<>();
                    claas.add(cla.getCaId());
                    majorService.updateClass(major.getMaId(), ArrayUtils.toString(claas));
                }
            }
        }
    }

    @Override
    public ClassEntity getClassById(Long claId) {
        ClassEntity classEntity = this.getById(claId);
        String studentIds = classEntity.getStudents();
        if (studentIds != null) {
            List<Long> longs = JSON.parseArray(studentIds, Long.class);
            List<StudentEntity> students = studentService.getStudentByIds(longs);
            classEntity.setStudentList(students);
        }
        return classEntity;
    }
    public List<ClassEntity> getClassByIds(List<Long> ids) {
        List<ClassEntity> classEntities = baseMapper.selectBatchIds(ids);
        List<StudentEntity> studentAll = studentService.getStudentAll();
        List<UserEntity> users = userService.list(new QueryWrapper<UserEntity>().eq("role", "3"));

        List<UserEntity> userEntities = new ArrayList<>();

        if (classEntities != null && studentAll != null) {
            return classEntities.stream().peek(classEntity -> {
                List<StudentEntity> studentEntities = studentService.getStudentByClassId(classEntity.getCaId());
                studentEntities.forEach(studentEntity-> {
                    users.forEach(userEntity -> {
                        if (userEntity.getUid().equals(studentEntity.getUid())) {
                            userEntity.setSchool(studentEntity);
                            userEntities.add(userEntity);
                        }
                    });
                });
                classEntity.setStudentList(userEntities);
            }).collect(Collectors.toList());
        }
        return classEntities;
    }

    /**
     * 不分页 并且返回班级信息
     * @return 班级
     */
    public List<ClassEntity> getClassAll() {
        List<ClassEntity> classAll = this.list();
        if (classAll == null) return null;
        List<Long> longs = classAll.stream().map(ClassEntity::getCaId).collect(Collectors.toList());
        return this.getClassByIds(longs);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class}, isolation = Isolation.DEFAULT)
    public boolean deleteSchoolClassById(Long caId, Long teaId, Long maId) {
        if (caId== null || maId == null) return false;
        boolean isDelete = this.removeById(caId);
        if (isDelete) {
            //更新专业的班级信息
            MajorEntity major = majorService.getById(maId);
            List<Long> classIds = JSON.parseArray(major.getClassIds(), Long.class);
            List<Long> newIds = classIds.stream().filter(id -> !id.equals(caId)).collect(Collectors.toList());
            majorService.updateClass(maId, ArrayUtils.toString(newIds));

            Optional.ofNullable(teaId)
                    .ifPresent(id -> {
                        //更新老师的班级信息
                        TeacherEntity teacher = teacherService.getTeacherById(id);
                        List<Long> teaClassIds = JSON.parseArray(teacher.getCaIds(), Long.class);
                        List<Long> newTeaClassIds = teaClassIds.stream().filter(fId -> !fId.equals(caId)).collect(Collectors.toList());
                        teacherService.updateClass(id, ArrayUtils.toString(newTeaClassIds));
                    });

            //更新学生的班级信息
            studentService.updateStudentsByClassId(caId, null);
            return true;
        }
        return false;
    }

    @Override
    public List<ClassEntity> getClassByMajorId(Long majorId) {
        return this.list(new QueryWrapper<ClassEntity>().eq("major_id", majorId));
    }

    /**
     * 老师查看所有班级学生的打卡列表
     * @param teacherId 老师id
     * @return 班级信息（包含每个班级所在的学生，以及打卡状况）
     */
    @Override
    public List<ClassEntity> getClassByTeacher(Long teacherId) {
        List<ClassEntity> classEntities = this.list(new QueryWrapper<ClassEntity>().eq("teacher_id", teacherId));
        String nowDate = DateUtilSweet.dateToString(new Date(), DateUtilSweet.DATE_FORMAT_DAY);
        String fifteenAgo = DateUtilSweet.getFifteenDaysAgo(DateUtilSweet.DATE_FORMAT_DAY);
        final int target = 15;
        classEntities.forEach(classEntity -> {
            List<StudentEntity> students = studentService.getStudentByClassId(classEntity.getCaId());
            List<HashMap<String, Object>> maps = students.stream().map(studentEntity -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put("stuName", studentEntity.getStuName());
                map.put("stuId", studentEntity.getStuId());
                map.put("uid", studentEntity.getUid());
                // 十五天打卡完成比
                QueryWrapper<ClockInEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("u_id", studentEntity.getUid()).and(w -> {
                    w.between("clock_in_time", fifteenAgo, nowDate);
                });
                int clockCount = clockInService.count(wrapper);
                float num = ((float)clockCount / target) * 100;
                map.put("clockPercentage", (int)num);
                map.put("count", clockCount);
                map.put("target", target);
                // 今日是否打卡
                ClockInEntity oneClockInByTime = clockInService.getOneClockInByTime(studentEntity.getUid());
                map.put("ClockInToday", (oneClockInByTime != null));
                return map;
            }).collect(Collectors.toList());
            classEntity.setStudentList(maps);
        });
        return classEntities;
    }

    @Override
    public void updateClassById(ClassEntity classEntity) {
        if (StringUtils.isNotEmpty(classEntity.getStudents())) {
            ClassEntity oneClass = getById(classEntity.getCaId());
            if (StringUtils.isNotBlank(oneClass.getStudents())) {
                List<Long> oldStudents = JSON.parseArray(oneClass.getStudents(), Long.class);
                List<Long> newStudents = JSON.parseArray(classEntity.getStudents(), Long.class);
                oldStudents.addAll(newStudents);
                classEntity.setStudents(ArrayUtils.toString(oldStudents));
            } else {
                List<Long> newStudents = JSON.parseArray(classEntity.getStudents(), Long.class);
                classEntity.setStudents(ArrayUtils.toString(newStudents));
            }
        }
        this.updateById(classEntity);
    }
}
