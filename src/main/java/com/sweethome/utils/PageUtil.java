package com.sweethome.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具
 */
@Data
public class PageUtil implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currPage;

    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currPage    当前页数
     */
    public PageUtil(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
    }

    /**
     * 处理 mybatis plus 分页
     */
    public PageUtil(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = (int)page.getTotal();
        this.pageSize = (int)page.getSize();
        this.currPage = (int)page.getCurrent();
        this.totalPage = (int)page.getPages();
    }
}
