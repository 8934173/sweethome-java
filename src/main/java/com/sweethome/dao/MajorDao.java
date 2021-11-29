package com.sweethome.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweethome.entities.MajorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MajorDao extends BaseMapper<MajorEntity> {
    void updateClass(@Param("ma_id") Long majorId, @Param("class_ids") String cla);
}
