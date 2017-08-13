package com.msqsoft.aboutapp.config;

/**
 * 常量
 */

public class Config {

    public static final String APP_KEY = "qf3d5gbjqfddh";//TODO 修改融云app-key
    public static final String APP_SECRET = "2FYiTXM8Ox4FcZ";//TODO 修改融云app-secret

    public static final int COUNT_DOWN_TIME = 60;//倒计时总时间
    public static final int MAX_PAGE_SIZE = 10;//每次请求返回条数

    //activity跳转请求码
    public static final int REQUEST_CODE_LOGIN = 1001;

    //SharePreferences相关
    public static final String APP_SETTING = "app_setting";//app设置
    public static final String USER_INFO = "user_info";//用户信息
    public static final String KEY_LAST_LOGIN_USER = "key_last_login_user";//最后登录用户的账号
    public static final String KEY_RONGIM_TOKEN = "key_rongim_token";//连接融云token
    public static final String KEY_ABOUTAPP_TOKEN = "key_aboutapp_token";//物流APPtoken
    public static final String KEY_ABOUTAPP_USER_ID = "key_aboutapp_user_id";//物流APP用户ID

    //错误码
    public static final String RESULT_CODE_SUCCESS = "100";//成功
    public static final String RESULT_CODE_TOKEN_INCORRECT = "206";//登录超时

}
