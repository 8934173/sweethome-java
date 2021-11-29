package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 权限
 */
@Data
@TableName("sys_author")
public class SysAuthor {

    /**
     * 权限 id
     */
    @TableId(value = "au_id", type = IdType.ASSIGN_ID)
    @NotEmpty
    private String auId;

    /**
     * 权限名
     */
    @NotNull
    private String auName;

    /**
     * 权限描述
     */
    private String auDescribe;

}
