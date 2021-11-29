package com.sweethome.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweethome.entities.ClassEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchoolClassDao extends BaseMapper<ClassEntity> {
}
