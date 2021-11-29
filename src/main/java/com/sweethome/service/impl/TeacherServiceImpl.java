package com.sweethome.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.TeacherDao;
import com.sweethome.entities.ClassEntity;
import com.sweethome.entities.TeacherEntity;
import com.sweethome.service.SchoolClassService;
import com.sweethome.service.TeacherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherDao, TeacherEntity> implements TeacherService {

    @Autowired
    SchoolClassService schoolClassService;

    @Override
    public List<TeacherEntity> getAllTeacher() {
        List<TeacherEntity> list = this.list();
        list.forEach(teacherEntity -> {
            String caIds = teacherEntity.getCaIds();
            if (caIds != null) {
                List<Long> longs = JSON.parseArray(caIds, Long.class);
                List<ClassEntity> classByIds = schoolClassService.getClassByIds(longs);
                teacherEntity.setClassEntities(classByIds);
            }
        });
        return list;
    }

    @Override
    public void updateClass(Long teaId, String caId) {
        this.baseMapper.updateClass(teaId, caId);
    }

    @Override
    public TeacherEntity getTeacherById(Long teaId) {
        return this.getById(teaId);
    }

    @Override
    public List<TeacherEntity> getTeacherByUid(List<String> uid) {
        List<TeacherEntity> teachers = this.baseMapper.getTeacherByUid(uid);
        teachers.forEach(teacherEntity -> {
            if (StringUtils.isNotBlank(teacherEntity.getCaIds())) {
                List<Long> claIds = JSON.parseArray(teacherEntity.getCaIds(), Long.class);
                List<ClassEntity> classEntities = schoolClassService.getClassByIds(claIds);
                teacherEntity.setClassEntities(classEntities);
            }
        });
        return teachers;
    }

    @Override
    public TeacherEntity getTeacherOneByUid(String uid) {
        return this.getOne(new QueryWrapper<TeacherEntity>().eq("uid", uid));
    }

    @Override
    public void deleteRoRecoverTeacherByUid(String uid, Integer status) {
        this.baseMapper.deleteRoRecoverTeacherByUid(uid, status);
    }
}
