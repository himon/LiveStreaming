package com.futang.livestreaming.data;

import com.futang.livestreaming.data.api.LiveApi;
import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.RoomEntity;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.account.RegisterNextActivity;
import com.futang.livestreaming.util.callback.CreateRoomCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
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

    public UserEntity getmUser() {
        return mUser;
    }

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


    public Observable<UserEntity> nextStep(File file, String userName, String sex, String loginId) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        // add another part within the multipart request
        RequestBody userNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), userName);
        RequestBody sexBody = RequestBody.create(MediaType.parse("multipart/form-data"), sex);
        RequestBody loginIdBody = RequestBody.create(MediaType.parse("multipart/form-data"), loginId);

        return mLiveApi.editUserInfo(body, userNameBody, sexBody, loginIdBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void startLive(File file, String roomId, String liveType, String liveName, String isCompany, CreateRoomCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUser.getBody().getId());
        params.put("roomId", roomId);
        params.put("liveType", liveType);
        params.put("liveName", liveName);
        params.put("isCompany", isCompany);
        params.put("token", mUser.getBody().getToken());

        String url = "http://112.74.21.82/App_API.ashx?action=UserLive";

        if (file != null) {
            OkHttpUtils.post()//
                    .addFile("picture", "image.png", file)//
                    .url(url)
                    .params(params)//
                    .build()//
                    .execute(callback);
        } else {
            OkHttpUtils.post().url(url).params(params).build().execute(callback);
        }
    }

    public Observable<RoomEntity> getRoomList(String liveType, String isCompany, String page) {
        return mLiveApi.getRoomList(liveType, isCompany, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
