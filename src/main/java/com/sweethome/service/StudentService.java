package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.StudentEntity;

import java.util.List;

public interface StudentService extends IService<StudentEntity> {
    List<StudentEntity> getStudentByIds(List<Long> ids);

    List<StudentEntity> getStudentAll();

    List<StudentEntity> getStudentByClassId(Long caId);

    void updateStudentsByClassId(Long caId, Long newCaId);

    List<StudentEntity> getStudentByUid(String uid);

    StudentEntity getStudentOneByUid(String uid);

    void deleteRoRecoverStudentByUid(String uid, Integer status);
}
