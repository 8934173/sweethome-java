package com.sweethome.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageVo {

    /**
     * 当前页
     */
    @NotNull
    private Long page;

    /**
     * 每一页条数
     */
    @NotNull
    private Long limit;

    /**
     * 开始时间
     */
    private String start;

    /**
     * 结束时间
     */
    private String end;

    /**
     * 关键字
     */
    private String keyWords;

    /**
     * 排序
     */
    private String order;

    /**
     * 排序字段
     */
    private String orderField;

    /**
     * 状态
     */
    private Integer status;

}
