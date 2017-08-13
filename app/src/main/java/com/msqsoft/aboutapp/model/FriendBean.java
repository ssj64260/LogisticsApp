package com.msqsoft.aboutapp.model;

/**
 * 朋友信息
 */

public class FriendBean {
    private String user_id;
    private String id;
    private String user_nicename;
    private String user_type;
    private String avatar;
    private String signature;
    private String mobile;
    private CourierBean courier_attrs;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public CourierBean getCourier_attrs() {
        return courier_attrs;
    }

    public void setCourier_attrs(CourierBean courier_attrs) {
        this.courier_attrs = courier_attrs;
    }
}
