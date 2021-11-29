package com.sweethome.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("leave_school")
public class LeaveEntity {

    @TableId(value = "out_id", type = IdType.ASSIGN_ID)
    @NotNull
    private Long outId;

    /**
     * 申请人名字
     */
    @TableField(value = "u_name")
    @NotNull
    private String uname;

    /**
     * 申请人电话
     */
    @NotNull
    private String phone;

    /**
     * 申请人所在班级
     */
    @NotNull
    private Long classId;

    /**
     * 监护人电话
     */
    @NotNull
    private String parentPhone;

    /**
     * 健康状况
     */
    @NotNull
    private String health;

    /**
     * 目的地
     */
    @NotNull
    private String destination;

    /**
     * 途径轨迹
     */
    private String track;

    /**
     * 离校时间
     */
    @NotNull
    private String leaveTime;

    /**
     * 预计返校时间
     */
    private String estimateReturnTime;

    /**
     * 审核老师id
     */
    @NotNull
    private Long teaId;

    /**
     * 审核老师姓名
     */
    @NotNull
    private String teaName;

    /**
     * 学生id
     */
    @NotNull
    private Long stuId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 外出事由
     */
    private String outReason;

    /**
     * 审核状态 1审核中 0 不通过 2通过
     */
    private Integer auditStatus;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 状态
     */
    @TableLogic(value = "1", delval = "0")
    private Integer status;
}
