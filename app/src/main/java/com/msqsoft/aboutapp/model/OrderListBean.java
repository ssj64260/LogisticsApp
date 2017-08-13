package com.msqsoft.aboutapp.model;

import java.util.List;

/**
 * 订单列表
 */

public class OrderListBean {
    private String date;//日期
    private List<OrderDetailBean> order;//当天订单列表

    //周边订单相关
    private String total_num;//订单总数
    private List<OrderDetailBean> orders;//订单列表

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<OrderDetailBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderDetailBean> order) {
        this.order = order;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public List<OrderDetailBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetailBean> orders) {
        this.orders = orders;
    }
}
