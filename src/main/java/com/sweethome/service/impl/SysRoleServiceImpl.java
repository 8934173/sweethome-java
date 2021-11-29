package com.sweethome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.SysRoleDao;
import com.sweethome.entities.SysRole;
import com.sweethome.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {
    @Override
    public SysRole selectRoleByUserId(String userId) {
        return this.getById(userId);
        //return baseMapper.selectRoleByUserId(userId);
    }

    @Override
    public List<SysRole> getAllRole() {
        return this.list();
    }
}
