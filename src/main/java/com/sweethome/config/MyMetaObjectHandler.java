package com.sweethome.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sweethome.utils.DateUtilSweet;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis plus 默认字段插入
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    private final Integer NORMAL = 1;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", DateUtilSweet.dateToString(new Date(), DateUtilSweet.DATE_FORMAT_DAY), metaObject);
        this.setFieldValByName("accountNonExpired", NORMAL, metaObject);
        this.setFieldValByName("accountNonLocked", NORMAL, metaObject);
        this.setFieldValByName("enabled", NORMAL, metaObject);
        this.setFieldValByName("credentialsNonExpired", NORMAL, metaObject);
        Object username = this.getFieldValByName("username", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
