package com.sweethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.UserDao;
import com.sweethome.entities.ClassEntity;
import com.sweethome.entities.StudentEntity;
import com.sweethome.entities.TeacherEntity;
import com.sweethome.entities.UserEntity;
import com.sweethome.service.SchoolClassService;
import com.sweethome.service.StudentService;
import com.sweethome.service.TeacherService;
import com.sweethome.service.UserService;
import com.sweethome.utils.PageUtil;
import com.sweethome.utils.Query;
import com.sweethome.vo.PageVo;
import com.sweethome.vo.UserEntityVo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private TeacherService teacherService;

    @Override
    public UserEntity queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class}, isolation = Isolation.DEFAULT)
    public void saveUser(UserEntityVo userEntity, Object role) {
        boolean save = this.save(userEntity);
        ClassEntity classEntity = new ClassEntity();
        classEntity.setCaId(userEntity.getCaId());
        if (save) {
            UserEntity user = this.getOne(new QueryWrapper<UserEntity>().eq("username", userEntity.getUsername()));
            if (role instanceof TeacherEntity) {
                TeacherEntity teacher = (TeacherEntity) role;
                teacher.setUid(user.getUid());
                teacherService.save(teacher);
                classEntity.setTeacherId(userEntity.getTeaId());
                classEntity.setTeacherName(userEntity.getTeaName());
                schoolClassService.updateById(classEntity);
            }
            if (role instanceof StudentEntity) {
                StudentEntity student = (StudentEntity) role;
                student.setUid(user.getUid());
                studentService.save(student);
                StudentEntity studentOneByUid = studentService.getStudentOneByUid(userEntity.getUid());
                classEntity.setStudents("["+studentOneByUid.getStuId()+"]");
                schoolClassService.updateClassById(classEntity);
            }
        }
    }

    @Override
    public PageUtil getUsersByAdmin(PageVo params) {
        // 查询
        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        String keyWords = params.getKeyWords();
        Integer status = params.getStatus();
        if (keyWords!= null || status != null) {
            userEntityQueryWrapper.eq("u_name", keyWords).or().eq("account_non_locked", status);
            IPage<UserEntity> users = this.page(
                    new Query<UserEntity>().getPage(params),
                    userEntityQueryWrapper
            );
            List<UserEntity> records = users.getRecords();
            records.forEach(userEntity -> {
                StudentEntity student = studentService.getStudentOneByUid(userEntity.getUid());
                if (student!=null) {
                    userEntity.setSchool(student);
                } else {
                    TeacherEntity teacher = teacherService.getTeacherOneByUid(userEntity.getUid());
                    userEntity.setSchool(teacher);
                }
            });
            users.setRecords(records);
            return new PageUtil(users);
        }

        //普通获取
        IPage<UserEntity> teacher = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>().ne("role", "3")
        );

        List<UserEntity> userTeachers = teacher.getRecords();
        List<String> uIds = userTeachers.stream().map(UserEntity::getUid).collect(Collectors.toList());
        List<TeacherEntity> teachers = teacherService.getTeacherByUid(uIds);
        userTeachers.forEach(teacherEntity -> {
            teachers.forEach(t -> {
                if (teacherEntity.getUid().equals(t.getUid())) {
                    teacherEntity.setSchool(t);
                }
            });
        });
        teacher.setRecords(userTeachers);
        return new PageUtil(teacher);
    }

    @Override
    public List<UserEntity> getUserAndSchoolRole(String uid) {
        List<UserEntity> userEntities = this.list(new QueryWrapper<UserEntity>().eq("uid", uid));
        List<StudentEntity> students = studentService.getStudentByUid(uid);
        userEntities.forEach(userEntity -> {
            Iterator<StudentEntity> iterator = students.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getUid().equals(userEntity.getUid())) {
                    userEntity.setSchool(iterator.next());
                    break;
                }
            }
        });
        return userEntities;
    }

    @Override
    public void lockOrUnlockAccount(String uid, Integer status) {
        this.baseMapper.lockOrUnlockAccount(uid, status);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class}, isolation = Isolation.DEFAULT)
    public void deleteAndUserByUid(String uid, Integer role) {
        this.removeById(uid);
        if (role== 2) {
            teacherService.deleteRoRecoverTeacherByUid(uid, 0);
        }
        if (role == 3) {
            studentService.deleteRoRecoverStudentByUid(uid, 0);
        }
    }
}
