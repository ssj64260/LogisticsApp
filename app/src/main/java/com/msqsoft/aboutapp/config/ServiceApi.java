package com.msqsoft.aboutapp.config;

import com.msqsoft.aboutapp.model.AddressBean;
import com.msqsoft.aboutapp.model.AreaBean;
import com.msqsoft.aboutapp.model.BannerBean;
import com.msqsoft.aboutapp.model.FirImBean;
import com.msqsoft.aboutapp.model.FriendBean;
import com.msqsoft.aboutapp.model.OrderListBean;
import com.msqsoft.aboutapp.model.RongIMTokenBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 网络请求接口 api
 */

public interface ServiceApi {

    String BASE_HOST = "http://test3.msqsoft.net/aboutapp/index.php/";

    //获取融云IMtoken
    @POST("http://api.cn.ronghub.com/user/getToken.json")
    @FormUrlEncoded
    Observable<RongIMTokenBean> getRongIMToken(
            @Header("App-Key") String appKey,
            @Header("Nonce") String nonce,
            @Header("Timestamp") String timestamp,
            @Header("Signature") String signature,
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("portraitUri") String portraitUri);

    //Fir更新app
    @GET("http://api.fir.im/apps/latest/5881b8c0959d691f5c00044c?api_token=e0be056f9e2f0e9623c5dd69b32e488c")
    Observable<FirImBean> checkUpdate();

    @POST("?g=WebApi&m=login&a=mobilesSignIn")//登录
    @FormUrlEncoded
    Observable<ServiceResult<UserInfoDetailBean>> doLogin(@Field("mobile") String mobile,
                                                          @Field("password") String password);

    @POST("?g=WebApi&m=login&a=sendPinSignUp")//注册获取验证码
    @FormUrlEncoded
    Observable<ServiceResult> getRegisterVerificationCode(@Field("mobile") String mobile);

    @POST("?g=WebApi&m=login&a=sendCode")//重置密码获取验证码
    @FormUrlEncoded
    Observable<ServiceResult> getVerificationCode(@Field("mobile") String mobile);

    @POST("?g=WebApi&m=user&a=ForgetPwd")//重置密码
    @FormUrlEncoded
    Observable<ServiceResult> doResetPassword(@Field("mobile") String mobile,
                                              @Field("code") String code,
                                              @Field("pwd") String password);

    @POST("?g=WebApi&m=login&a=mobilesSignUp")//注册
    @FormUrlEncoded
    Observable<ServiceResult<UserInfoDetailBean>> doRegister(@Field("mobile") String mobile,
                                                             @Field("code") String code,
                                                             @Field("password") String password);

    //获取首页广告
    @GET("?g=WebApi&m=basic&a=getslide")
    Observable<ServiceResult<List<BannerBean>>> getBannerList();

    @POST("?g=WebApi&m=basic&a=getArea")//获取省市区
    @FormUrlEncoded
    Observable<ServiceResult<List<AreaBean>>> getArea(@Field("pid") String pid);

    @POST("?g=WebApi&m=address&a=getPosition")//提交定位
    @FormUrlEncoded
    Observable<ServiceResult<Map<String, String>>> getPosition(@Field("area") String area);


    ///////////////////////////////////////////////////////////////////////////
    // 需要验证token的接口
    ///////////////////////////////////////////////////////////////////////////
    @POST("?g=WebApi&m=user&a=getUserInfo")//获取当前登录用户信息
    @FormUrlEncoded
    Observable<ServiceResult<UserInfoDetailBean>> getUserInfoDetail(@Header("apiToken") String token,
                                                                    @Field("user_id") String userId);

    @POST("?g=WebApi&m=user&a=updateUserInfo")//修改用户昵称，个人签名，手机号
    @FormUrlEncoded
    Observable<ServiceResult> updateUserInfo(@Header("apiToken") String token,
                                             @Field("user_nicename") String nickname,
                                             @Field("signature") String sign,
                                             @Field("mobile") String mobile);

    @POST("?g=WebApi&m=user&a=avatarUpload")//上传头像
    @Multipart
    Observable<ServiceResult> uploadUserAvatar(@Header("apiToken") String token,
                                               @Part MultipartBody.Part file);

    @POST("?g=WebApi&m=user&a=resetPwd")//修改密码
    @FormUrlEncoded
    Observable<ServiceResult> doChangePassword(@Header("apiToken") String token,
                                               @Field("code") String code,
                                               @Field("pwd") String password);

    @POST("?g=WebApi&m=orders&a=getmyorders")//获取自己发布的订单
    @FormUrlEncoded
    Observable<ServiceResult<List<OrderListBean>>> getMyOrderList(@Header("apiToken") String token,
                                                                  @Field("page") String page,
                                                                  @Field("page_size") String pageSize,
                                                                  @Field("status") String status,
                                                                  @Field("goods_kind_id") String goodsKindId,
                                                                  @Field("express_company_id") String companyId);

    @POST("?g=WebApi&m=orders&a=getExpressOrders")//获取快递员接收的订单
    @FormUrlEncoded
    Observable<ServiceResult<List<OrderListBean>>> getReceiveOrderList(@Header("apiToken") String token,
                                                                       @Field("page") String page,
                                                                       @Field("page_size") String pageSize,
                                                                       @Field("status") String status,
                                                                       @Field("goods_kind_id") String goodsKindId,
                                                                       @Field("express_company_id") String companyId);

    @POST("?g=WebApi&m=orders&a=getAroundOrders")//获取周边订单列表
    @FormUrlEncoded
    Observable<ServiceResult<OrderListBean>> getAroundOrderList(@Header("apiToken") String token,
                                  @Field("page") String page,
                                  @Field("page_size") String pageSize,
                                  @Field("status") String status,
                                  @Field("publish_way") String publishWay,
                                  @Field("express_company_id") String companyId,
                                  @Field("area_id") String areaId);

    //获取好友列表
    @GET("?g=WebApi&m=user&a=getFriendsList")
    Observable<ServiceResult<List<FriendBean>>> getFriendsList(@Header("apiToken") String token);

    //获取地址列表
    @GET("?g=WebApi&m=address&a=get")
    Observable<ServiceResult<List<AddressBean>>> getAddressList(@Header("apiToken") String token);

    @POST("?g=WebApi&m=address&a=edit")//编辑地址
    @FormUrlEncoded
    Observable<ServiceResult> editAddress(@Header("apiToken") String token,
                                          @Field("name") String name, @Field("mobile") String mobile, @Field("area_id") String areaId,
                                          @Field("detail") String detail, @Field("status") String status, @Field("aid") String aid);

    @POST("?g=WebApi&m=address&a=delete")//删除地址
    @FormUrlEncoded
    Observable<ServiceResult> deleteAddress(@Header("apiToken") String token,
                                            @Field("aid") String aid);

    @POST("?g=WebApi&m=address&a=add")//添加地址
    @FormUrlEncoded
    Observable<ServiceResult> addAddress(@Header("apiToken") String token,
                                         @Field("name") String name, @Field("mobile") String mobile, @Field("area_id") String areaId,
                                         @Field("detail") String detail, @Field("status") String status);

}
