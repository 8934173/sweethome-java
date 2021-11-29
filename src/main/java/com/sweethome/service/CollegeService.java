package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.CollegeEntity;

import java.util.List;

public interface CollegeService extends IService<CollegeEntity> {
    boolean saveCollegeOne(CollegeEntity college);

    List<CollegeEntity> getCollegeList();

    boolean deleteCollegeById(Long coId);
}
