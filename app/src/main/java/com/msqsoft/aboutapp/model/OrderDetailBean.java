package com.msqsoft.aboutapp.model;

import android.text.TextUtils;

/**
 * 订单详情
 */

public class OrderDetailBean {

    private String id;//订单ID
    private String code;//订单号
    private String express_company_id;//快递公司ID
    private String rank;//
    private String status;//订单状态（0 删除 1 未确认 2 待收件 3 待转出 4 已转出 5 已签收 6.获取待评论）
    private String publish_way;//发布途径
    private String create_time;//创建时间
    private String goods_kind_name;//货物类型名称
    private String sender_area_id;//发送地址ID
    private String sender_area_detail;//发送详细地址
    private String recipient_area_id;//收货地址ID
    private String recipient_area_detail;//收货详细地址
    private String user_nicename;//用户昵称
    private String avatar;//用户头像
    private String user_login;//登录用户帐号
    private String user_id;//用户ID
    private String courier_nicename;//快递员昵称
    private String courier_avatar;//快递员头像
    private String courier_login;//快递员登录帐号
    private String courier_id;//快递员ID
    private boolean has_comment;//是否有评论
    private String sender_area_name;//发送地址省市
    private String recipient_area_name;//收货地址省市

    public String getOrderStatus() {
        String statusName = "";
        if (!TextUtils.isEmpty(status)) {
            switch (status) {
                case "0":
                    statusName = "已删除";
                    break;
                case "1":
                    statusName = "未确认";
                    break;
                case "2":
                    statusName = "待收件";
                    break;
                case "3":
                    statusName = "待转出";
                    break;
                case "4":
                    statusName = "已转出";
                    break;
                case "5":
                    statusName = "已签收";
                    break;
                case "6":
                    statusName = "待评价";
                    break;
            }
        }
        return statusName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpress_company_id() {
        return express_company_id;
    }

    public void setExpress_company_id(String express_company_id) {
        this.express_company_id = express_company_id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublish_way() {
        return publish_way;
    }

    public void setPublish_way(String publish_way) {
        this.publish_way = publish_way;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGoods_kind_name() {
        return goods_kind_name;
    }

    public void setGoods_kind_name(String goods_kind_name) {
        this.goods_kind_name = goods_kind_name;
    }

    public String getSender_area_id() {
        return sender_area_id;
    }

    public void setSender_area_id(String sender_area_id) {
        this.sender_area_id = sender_area_id;
    }

    public String getSender_area_detail() {
        return sender_area_detail;
    }

    public void setSender_area_detail(String sender_area_detail) {
        this.sender_area_detail = sender_area_detail;
    }

    public String getRecipient_area_id() {
        return recipient_area_id;
    }

    public void setRecipient_area_id(String recipient_area_id) {
        this.recipient_area_id = recipient_area_id;
    }

    public String getRecipient_area_detail() {
        return recipient_area_detail;
    }

    public void setRecipient_area_detail(String recipient_area_detail) {
        this.recipient_area_detail = recipient_area_detail;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCourier_nicename() {
        return courier_nicename;
    }

    public void setCourier_nicename(String courier_nicename) {
        this.courier_nicename = courier_nicename;
    }

    public String getCourier_avatar() {
        return courier_avatar;
    }

    public void setCourier_avatar(String courier_avatar) {
        this.courier_avatar = courier_avatar;
    }

    public String getCourier_login() {
        return courier_login;
    }

    public void setCourier_login(String courier_login) {
        this.courier_login = courier_login;
    }

    public String getCourier_id() {
        return courier_id;
    }

    public void setCourier_id(String courier_id) {
        this.courier_id = courier_id;
    }

    public boolean isHas_comment() {
        return has_comment;
    }

    public void setHas_comment(boolean has_comment) {
        this.has_comment = has_comment;
    }

    public String getSender_area_name() {
        return sender_area_name;
    }

    public void setSender_area_name(String sender_area_name) {
        this.sender_area_name = sender_area_name;
    }

    public String getRecipient_area_name() {
        return recipient_area_name;
    }

    public void setRecipient_area_name(String recipient_area_name) {
        this.recipient_area_name = recipient_area_name;
    }
}
