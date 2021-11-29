package com.sweethome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.SysAuthorDao;
import com.sweethome.entities.SysAuthor;
import com.sweethome.service.SysAuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAuthorServiceImpl extends ServiceImpl<SysAuthorDao, SysAuthor> implements SysAuthorService {

    @Override
    public List<SysAuthor> getAuthorByIds(List<String> auIds) {
        return this.listByIds(auIds);
    }
}
