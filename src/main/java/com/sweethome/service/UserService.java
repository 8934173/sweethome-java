package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.UserEntity;
import com.sweethome.utils.PageUtil;
import com.sweethome.vo.PageVo;
import com.sweethome.vo.UserEntityVo;

import java.util.List;

public interface UserService extends IService<UserEntity> {
    UserEntity queryByUserName(String username);

    void saveUser(UserEntityVo userEntity, Object role);

    PageUtil getUsersByAdmin(PageVo params);

    List<UserEntity> getUserAndSchoolRole(String uid);

    void lockOrUnlockAccount(String uid, Integer status);

    void deleteAndUserByUid(String uid, Integer role);
}
