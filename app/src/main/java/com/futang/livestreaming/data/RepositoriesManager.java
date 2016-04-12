package com.futang.livestreaming.data;

import com.futang.livestreaming.data.api.LiveApi;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.account.RegisterNextActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/8.
 */
public class RepositoriesManager {

    private UserEntity mUser;
    private LiveApi mLiveApi;

    public RepositoriesManager(UserEntity user, LiveApi liveApi) {
        this.mUser = user;
        this.mLiveApi = liveApi;
    }

    public void nextStep(File file, String userName, String sex, String loginId, RegisterNextActivity.MyStringCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("username", userName);
        params.put("sex", sex);
        params.put("loginId", mUser.getBody().getLoginId());

        String url = "http://112.74.21.82/App_API.ashx?action=ModefUser";
        OkHttpUtils.post()//
                .addFile("picture", "image.png", file)//
                .url(url)
                .params(params)//
                .build()//
                .execute(callback);
    }
}
