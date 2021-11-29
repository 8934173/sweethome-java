package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    SysRole selectRoleByUserId(String userId);

    List<SysRole> getAllRole();
}
