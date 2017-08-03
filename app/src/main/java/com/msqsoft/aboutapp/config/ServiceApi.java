package com.msqsoft.aboutapp.config;

import com.msqsoft.aboutapp.model.BannerBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    @POST("?g=WebApi&m=login&a=sendPinSignUp")//注册获取验证码
    @FormUrlEncoded
    Observable<ServiceResult> getRegisterVerificationCode(@Field("mobile") String mobile);

    @POST("?g=WebApi&m=login&a=sendCode")//重置密码获取验证码
    @FormUrlEncoded
    Observable<ServiceResult> getVerificationCode(@Field("mobile") String mobile);

    @POST("?g=WebApi&m=user&a=ForgetPwd")//重置密码
    @FormUrlEncoded
    Observable<ServiceResult> doSetPassword(@Field("mobile") String mobile, @Field("code") String code, @Field("pwd") String password);

    @POST("?g=WebApi&m=login&a=mobilesSignUp")//注册
    @FormUrlEncoded
    Observable<ServiceResult<UserInfoDetailBean>> doRegister(@Field("mobile") String mobile, @Field("code") String code, @Field("password") String password);

    @GET("?g=WebApi&m=basic&a=getslide")
    Observable<ServiceResult<List<BannerBean>>> getBannerList();
}
