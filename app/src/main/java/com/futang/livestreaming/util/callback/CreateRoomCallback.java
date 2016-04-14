package com.futang.livestreaming.util.callback;

import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.UserEntity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class CreateRoomCallback extends Callback<CreateRoomEntity> {
    @Override
    public CreateRoomEntity parseNetworkResponse(Response response) throws IOException {
        String string = response.body().string();
        CreateRoomEntity room = new Gson().fromJson(string, CreateRoomEntity.class);
        return room;
    }


}
