package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sweethome.valid.AddGroup;
import com.sweethome.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@TableName(value = "college")
public class CollegeEntity {

    /**
     * 学院id
     */
    @TableId(value = "co_id", type = IdType.ASSIGN_ID)
    @NotNull(message = "id 能为空", groups = {UpdateGroup.class})
    @Null(message = "不能指定id", groups = {AddGroup.class})
    private Long coId;

    /**
     * 专业名称
     */
    @NotNull
    @NotBlank
    private String collegeName;

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

    /**
     * 专业
     */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MajorEntity> majors;

}
