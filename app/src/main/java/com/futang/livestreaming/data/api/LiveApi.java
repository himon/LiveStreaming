package com.futang.livestreaming.data.api;

import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.RoomEntity;
import com.futang.livestreaming.data.entity.UserEntity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/4/7.
 */
public interface LiveApi {

    @FormUrlEncoded
    @POST("App_API.ashx?action=Register")
    Observable<UserEntity> register(@Field("phone") String phone, @Field("loginType") String loginType, @Field("loginPwd") String loginPwd, @Field("recommendUser") String recommendUser, @Field("isCompany") String isCompany);

    @FormUrlEncoded
    @POST("App_API.ashx?action=QQLogin")
    Observable<UserEntity> login(@Field("qq") String qq, @Field("userName") String userName, @Field("sex") String sex, @Field("ico") String ico, @Field("loginType") String loginType, @Field("isCompany") String isCompany);

    @FormUrlEncoded
    @POST("App_API.ashx?action=LoginCheck")
    Observable<UserEntity> login(@Field("loginId") String loginId, @Field("loginPwd") String loginPwd, @Field("loginType") String loginType);


    @Multipart
    @POST("App_API.ashx?action=ModefUser")
    Observable<UserEntity> editUserInfo(@Part("picture") MultipartBody.Part file, @Part("userName") RequestBody userName, @Part("sex") RequestBody sex, @Part("loginId") RequestBody loginId);

    @FormUrlEncoded
    @POST("App_API.ashx?action=OnlineList")
    Observable<RoomEntity> getRoomList(@Field("liveType") String liveType, @Field("isCompany") String isCompany, @Field("page") String page);
}


