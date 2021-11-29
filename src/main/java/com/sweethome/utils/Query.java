package com.sweethome.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sweethome.vo.PageVo;
import org.springframework.util.StringUtils;

import java.util.Map;

public class Query <T>{

    private final String ASC = "asc";

    /**
     *
     * @param params 分页参数
     * @param defaultOrderField 其他参数
     * @param isAsc 是否排序 Map<String, Object> params
     * @return IPage
     */
    public IPage<T> getPage(PageVo params, String defaultOrderField, boolean isAsc) {
        long curPage = 1;
        long limit = 10;
        if (params.getPage() != null) {
            curPage = params.getPage();
        }
        if (params.getLimit() != null) {
            limit = params.getLimit();
        }
        Page<T> page = new Page<>(curPage, limit);

        String order = params.getOrder();
        String orderField = params.getOrderField();

        // params 前端获取
        if (StringUtils.hasLength(order) && StringUtils.hasLength(orderField)) {
            if (ASC.equalsIgnoreCase(order)) { //升
                return page.addOrder(OrderItem.asc(orderField));
            } else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        // defaultOrderField 以及 isAsc 后端处理排序 ,defaultOrderField 为null则默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }
        return page;
    }
    public IPage<T> getPage(PageVo vo) {
        return this.getPage(vo, null, false);
    }
}
