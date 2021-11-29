package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("student")
public class StudentEntity {
    /**
     * 学生id
     */
    @TableId(value = "stu_id", type = IdType.ASSIGN_ID)
    @NotNull
    private Long stuId;

    /**
     * 学生名字
     */
    private String stuName;

    /**
     * 教室id 教室对应班主任，这样学生和班主任就有对应关系
     */
    private Long caId;

    /**
     * 教室名称
     */
    private String claName;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 状态
     */
    private Integer status;

}
