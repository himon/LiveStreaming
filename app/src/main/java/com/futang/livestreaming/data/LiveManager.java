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

    public Observable<UserEntity> register(String phone, String loginPwd) {
        return getLiveApi().register(phone, "0", loginPwd, "0", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UserEntity> login(String phone, String loginPwd) {
        return getLiveApi().login(phone, "0", loginPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
