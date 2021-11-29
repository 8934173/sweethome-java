package com.sweethome.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.MajorDao;
import com.sweethome.entities.ClassEntity;
import com.sweethome.entities.MajorEntity;
import com.sweethome.service.MajorService;
import com.sweethome.service.SchoolClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MajorServiceImpl extends ServiceImpl<MajorDao, MajorEntity> implements MajorService {

    @Autowired
    private SchoolClassService schoolClassService;

    @Override
    public void saveMajor(MajorEntity major) {
        this.saveOrUpdate(major);
    }

    @Override
    public List<MajorEntity> getMajorByCollegeId(Long collegeId) {
        QueryWrapper<MajorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("college_id", collegeId);
        return this.list(wrapper);
    }

    /**
     * 获取所有专业信息，包含该专业的所有学校信息
     * @return 专业列表
     */
    @Override
    public List<MajorEntity> getMajorAll() {
        List<MajorEntity> majors = this.list();
        List<ClassEntity> classAll = schoolClassService.getClassAll();
        if (classAll != null) {
            return majors.stream().peek(majorEntity -> {
                String classIds = majorEntity.getClassIds();
                if (classIds != null) {
                    List<Long> cla = JSON.parseArray(classIds, Long.class);
                    List<ClassEntity> classByIds = schoolClassService.getClassByIds(cla);
                    majorEntity.setClassEntities(classByIds);
                }
            }).collect(Collectors.toList());
        }
        return majors;
    }

    @Override
    public void updateClass(Long majorId, String classIds) {
        this.baseMapper.updateClass(majorId, classIds);
    }

    @Override
    public MajorEntity getMajorById(Long maId) {
        return this.getById(maId);
    }

    /**
     * 专业下面有班级不能删除
     * @param maId 专业id
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class}, isolation = Isolation.DEFAULT)
    public boolean deleteMajorById(Long maId) {
        if (maId == null) return false;
        MajorEntity major = this.getById(maId);
        if (major != null && major.getClassIds() != null && StringUtils.isNotBlank(major.getClassIds())) return false;
        return this.removeById(maId);
    }
}
