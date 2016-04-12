package com.futang.livestreaming.data;

import com.futang.livestreaming.data.api.LiveApi;
import com.futang.livestreaming.data.entity.UserEntity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/7.
 */
public class LiveManager {

    private LiveApi liveApi;

    public LiveApi getLiveApi() {
        return liveApi;
    }

    public LiveManager(LiveApi liveApi) {
        this.liveApi = liveApi;
    }

    /**
     * 手机注册
     *
     * @param phone
     * @param loginPwd
     * @return
     */
    public Observable<UserEntity> register(String phone, String loginPwd) {
        return getLiveApi().register(phone, "0", loginPwd, "0", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 手机登录
     *
     * @param phone
     * @param loginPwd
     * @return
     */
    public Observable<UserEntity> login(String phone, String loginPwd) {
        return getLiveApi().login(phone, loginPwd, "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 第三方登录
     *
     * @param qq
     * @param userName
     * @param sex
     * @param ico
     * @param loginType
     * @param isCompany
     * @return
     */

    public Observable<UserEntity> quickLogin(String qq, String userName, String sex, String ico, String loginType, String isCompany) {
        return getLiveApi().login(qq, userName, sex, ico, loginType, isCompany)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
