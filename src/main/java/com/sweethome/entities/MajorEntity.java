package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sweethome.valid.AddGroup;
import com.sweethome.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 专业 实体类
 */
@Data
@TableName(value = "major")
public class MajorEntity {

    /**
     * 专业名称
     */
    @NotNull(message = "修改数据maId不能为空", groups = {UpdateGroup.class})
    @Null(message = "插入不能指定id", groups = {AddGroup.class})
    @TableId(value = "ma_id", type = IdType.ASSIGN_ID)
    private Long maId;

    /**
     * 专业名称
     */
    @NotNull
    private String majorName;

    /**
     * 院系 id
     */
    private Long collegeId;

    /**
     * 院系名称
     */
    private String collegeName;

    /**
     * 班级id [,] 数组字符串
     */
    private String classIds;

    /**
     * 所有班级信息
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<ClassEntity> classEntities;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 状态
     */
    @TableLogic(value = "1", delval = "0")
    @TableField(value = "status")
    private Integer status;
}
