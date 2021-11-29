package com.sweethome.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweethome.entities.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {
    SysRole selectRoleByUserId(@Param("user_id") String userId);
}
