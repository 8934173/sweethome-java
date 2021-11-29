package com.sweethome.entities;


import com.baomidou.mybatisplus.annotation.*;
import com.sweethome.valid.AddGroup;
import com.sweethome.valid.UpdateGroup;
import com.sweethome.valid.annotation.ListValue;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@TableName("clock_in")
public class ClockInEntity {

    /**
     * id
     */
    @TableId(value = "c_id", type = IdType.ASSIGN_ID)
    @Null(message = "不能指定用户id", groups = AddGroup.class)
    @NotNull(message = "用户ID不能为空", groups = UpdateGroup.class)
    private String cid;

    /**
     * 打卡时间
     */
    @NotNull(message = "打卡时间不能为空")
    private String clockInTime;

    @TableField(value = "u_name")
    private String uname;

    /**
     * 体温
     */
    @NotNull(message = "体温不能为空")
    private Float temperature;

    /**
     * 健康状况 1正常 0不正常
     */
    @ListValue(values = {0, 1}, groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer health;

    /**
     * 打卡地址
     */
    @NotEmpty(message = "地址不能为空")
    private String location;

    /**
     * 是否去过中高风险地区 1否 0是
     */
    @ListValue(values = {0, 1}, groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer riskAreas;

    /**
     * 是否接触过疑似或确诊 1否 0是
     */
    @ListValue(values = {0, 1}, groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer suspected;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 状态 1可见 0不可见
     */
    @TableLogic(value = "1", delval = "0")
    @TableField(value = "status")
    private Integer status;

    @NotNull(message = "打卡用户不能为空", groups = {AddGroup.class})
    @TableField(value = "u_id")
    private String uid;
}
