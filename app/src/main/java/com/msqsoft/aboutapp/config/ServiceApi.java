package com.msqsoft.aboutapp.config;

import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 网络请求接口 api
 */

public interface ServiceApi {

    @POST("?g=WebApi&m=login&a=mobilesSignIn")//登录
    @FormUrlEncoded
    Observable<ServiceResult<UserInfoDetailBean>> doLogin(@Field("mobile") String mobile, @Field("password") String password);

    @POST("?g=WebApi&m=user&a=getUserInfo")//获取当前登录用户信息
    @FormUrlEncoded
    Observable<ServiceResult<UserInfoDetailBean>> getUserInfoDetail(@Header("apiToken") String token, @Field("user_id") String userId);

    @POST("?g=WebApi&m=login&a=sendPinSignUp")//获取验证码
    @FormUrlEncoded
    Observable<ServiceResult> getVerificationCode(@Field("mobile") String mobile);

    @POST("?g=WebApi&m=user&a=ForgetPwd")//提交手机号码，验证码，密码
    @FormUrlEncoded
    Observable<ServiceResult> doSetPassword(@Field("mobile") String mobile, @Field("code") String code, @Field("pwd") String password);

}
