package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@TableName("teacher")
public class TeacherEntity {

    /**
     * 教师id
     */
    @TableId(value = "tea_id", type = IdType.ASSIGN_ID)
    private Long teaId;

    /**
     * 教师名字
     */
    @NotNull
    private String teaName;

    /**
     * 学院id
     */
    @NotNull
    private Long collegeId;

    /**
     * 用户id 对应所有用户的表
     */
    @NotNull
    private String uid;

    /**
     * 所带领的班级 [] 数组模板字符串
     */
    private String caIds;

    /**
     * 班级
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ClassEntity> classEntities;

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
