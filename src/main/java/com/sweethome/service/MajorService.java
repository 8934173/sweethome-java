package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.MajorEntity;

import java.util.List;

public interface MajorService extends IService<MajorEntity> {
    void saveMajor(MajorEntity major);

    List<MajorEntity> getMajorByCollegeId(Long collegeId);

    List<MajorEntity> getMajorAll();

    void updateClass(Long majorId, String classIds);

    MajorEntity getMajorById(Long maId);

    boolean deleteMajorById(Long maId);
}
