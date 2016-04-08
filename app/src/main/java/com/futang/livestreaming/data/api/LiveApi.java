package com.futang.livestreaming.data.api;

import com.futang.livestreaming.data.entity.UserEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    @POST("App_API.ashx?action=Register")
    Observable<UserEntity> login(@Field("loginID") String loginID, @Field("loginType") String loginType, @Field("loginPwd") String loginPwd);

}
