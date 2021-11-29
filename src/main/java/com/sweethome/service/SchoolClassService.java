package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.ClassEntity;

import java.util.List;

public interface SchoolClassService extends IService<ClassEntity> {
    void saveSchoolClass(ClassEntity classEntity);

    ClassEntity getClassById(Long claId);

    List<ClassEntity> getClassByIds(List<Long> ids);

    List<ClassEntity> getClassAll();

    boolean deleteSchoolClassById(Long caId, Long teaId, Long maId);

    List<ClassEntity> getClassByMajorId(Long majorId);

    List<ClassEntity> getClassByTeacher(Long teacherId);

    void updateClassById(ClassEntity classEntity);
}
