package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.TeacherEntity;

import java.util.List;

public interface TeacherService extends IService<TeacherEntity> {
    List<TeacherEntity> getAllTeacher();

    void updateClass(Long teaId, String caId);

    TeacherEntity getTeacherById(Long teaId);

    List<TeacherEntity> getTeacherByUid(List<String> uid);

    TeacherEntity getTeacherOneByUid(String uid);

    void deleteRoRecoverTeacherByUid(String uid, Integer status);
}
