package com.sweethome.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweethome.entities.LeaveEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LeaveDao extends BaseMapper<LeaveEntity> {
}
