package com.sweethome.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweethome.entities.SysRole;
import com.sweethome.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
    UserEntity queryByUserName(@Param("username") String username);

    void lockOrUnlockAccount(@Param("uid") String uid, @Param("status") Integer status);

    void deleteUserByUid(@Param("uid") String uid, @Param("enabled") Integer enabled);
}
