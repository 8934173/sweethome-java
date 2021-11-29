package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.SysAuthor;

import java.util.List;

public interface SysAuthorService extends IService<SysAuthor> {
    List<SysAuthor> getAuthorByIds(List<String> auIds);
}
