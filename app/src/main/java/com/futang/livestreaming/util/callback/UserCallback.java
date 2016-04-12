package com.futang.livestreaming.util.callback;

import com.futang.livestreaming.data.entity.UserEntity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class UserCallback extends Callback<UserEntity> {
    @Override
    public UserEntity parseNetworkResponse(Response response) throws IOException {
        String string = response.body().string();
        UserEntity user = new Gson().fromJson(string, UserEntity.class);
        return user;
    }


}
