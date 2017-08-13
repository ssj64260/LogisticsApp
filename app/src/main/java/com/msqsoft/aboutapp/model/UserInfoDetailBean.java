package com.msqsoft.aboutapp.model;

import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.util.List;

/**
 * 用户登录信息
 */

@Table("UserInfoDetailBean")
public class UserInfoDetailBean {

    @PrimaryKey(AssignType.BY_MYSELF)
    private String id;
    private String user_login;
    private String user_pass;
    private String user_nicename;
    private String user_email;
    private String user_url;
    private String avatar;
    private String sex;
    private String birthday;
    private String signature;
    private String last_login_ip;
    private String last_login_time;
    private String create_time;
    private String user_activation_key;
    private String user_status;
    private String score;
    private String user_type;//1:admin，2:普通会有，3:快递员，4:店主
    private String coin;
    private String mobile;
    private String access_token;
    private String role_id;

    private String longitude;
    private String latitude;
    private String employee_num;
    private String handle_orders_count;
    private String express_company_name;
    private String express_mobile;
    private String express_company_icon;
    private String sender_area_detail;
    private String sender_area_name;

    @Ignore
    private List<CommentBean> user_comment;

    public void setUserInfo(String nickname, String avatar, String signature, String mobile) {
        setUser_nicename(nickname);
        setAvatar(avatar);
        setSignature(signature);
        setMobile(mobile);
    }

    public void setCourierInfo(UserInfoDetailBean courierInfo) {
        setLongitude(courierInfo.getLongitude());
        setLatitude(courierInfo.getLatitude());
        setEmployee_num(courierInfo.getEmployee_num());
        setHandle_orders_count(courierInfo.getHandle_orders_count());
        setExpress_company_name(courierInfo.getExpress_company_name());
        setExpress_mobile(courierInfo.getExpress_mobile());
        setExpress_company_icon(courierInfo.getExpress_company_icon());
        setSender_area_detail(courierInfo.getSender_area_detail());
        setSender_area_name(courierInfo.getSender_area_name());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_activation_key() {
        return user_activation_key;
    }

    public void setUser_activation_key(String user_activation_key) {
        this.user_activation_key = user_activation_key;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEmployee_num() {
        return employee_num;
    }

    public void setEmployee_num(String employee_num) {
        this.employee_num = employee_num;
    }

    public String getHandle_orders_count() {
        return handle_orders_count;
    }

    public void setHandle_orders_count(String handle_orders_count) {
        this.handle_orders_count = handle_orders_count;
    }

    public String getExpress_company_name() {
        return express_company_name;
    }

    public void setExpress_company_name(String express_company_name) {
        this.express_company_name = express_company_name;
    }

    public String getExpress_mobile() {
        return express_mobile;
    }

    public void setExpress_mobile(String express_mobile) {
        this.express_mobile = express_mobile;
    }

    public String getExpress_company_icon() {
        return express_company_icon;
    }

    public void setExpress_company_icon(String express_company_icon) {
        this.express_company_icon = express_company_icon;
    }

    public String getSender_area_detail() {
        return sender_area_detail;
    }

    public void setSender_area_detail(String sender_area_detail) {
        this.sender_area_detail = sender_area_detail;
    }

    public String getSender_area_name() {
        return sender_area_name;
    }

    public void setSender_area_name(String sender_area_name) {
        this.sender_area_name = sender_area_name;
    }

    public List<CommentBean> getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(List<CommentBean> user_comment) {
        this.user_comment = user_comment;
    }
}
