package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sweethome.valid.AddGroup;
import com.sweethome.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@TableName("cla_ss")
public class ClassEntity {

    /**
     * 班级 id
     */
    @NotNull(message = "跟新请指定caId", groups = {UpdateGroup.class})
    @Null(message = "插入不能指定caId", groups = {AddGroup.class})
    @TableId(value = "ca_id", type = IdType.ASSIGN_ID)
    private Long caId;

    /**
     * 班级 名字
     */
    @NotBlank
    @Max(message = "claName超出最大长度限制", value = 15, groups = {AddGroup.class, UpdateGroup.class})
    private String claName;

    /**
     * 院系id
     */
    @NotNull(groups = {UpdateGroup.class})
    private Long collegeId;

    /**
     * 院系名字
     */
    @NotBlank(groups = {UpdateGroup.class})
    private String collegeName;

    /**
     * 专业 id
     */
    @NotNull(groups = {UpdateGroup.class})
    private Long majorId;

    /**
     * 专业 名字
     */
    @NotBlank(groups = {UpdateGroup.class})
    private String majorName;

    /**
     * 老师（班主任） id
     */
    @NotNull(groups = {UpdateGroup.class})
    private Long teacherId;

    /**
     * 老师名字
     */
    @NotBlank(groups = {UpdateGroup.class})
    private String teacherName;

    /**
     * 班级所在的学生 [,] 数组字符串 学生id
     */
    private String students;

    /**
     * 学生列表
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object studentList;

    /**
     * 状态
     */
    @TableLogic(value = "1", delval = "0")
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
}
