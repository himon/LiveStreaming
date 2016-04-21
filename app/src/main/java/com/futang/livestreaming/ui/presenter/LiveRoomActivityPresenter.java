package com.futang.livestreaming.ui.presenter;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.GiftEntity;
import com.futang.livestreaming.data.entity.StopRoomEntity;
import com.futang.livestreaming.ui.view.ILiveRoomView;
import com.futang.livestreaming.util.callback.CreateRoomCallback;
import com.futang.livestreaming.util.observer.SimpleObserver;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/16.
 */
public class LiveRoomActivityPresenter {

    private ILiveRoomView mILiveRoomView;
    private RepositoriesManager mRepositoriesManager;

    public RepositoriesManager getmRepositoriesManager() {
        return mRepositoriesManager;
    }

    public LiveRoomActivityPresenter(ILiveRoomView iLiveRoomView, RepositoriesManager repositoriesManager) {
        this.mILiveRoomView = iLiveRoomView;
        this.mRepositoriesManager = repositoriesManager;
    }

    public void startLive(File file, String roomId, String liveType, String liveName, String isCompany, String rtmp, String hls) {
        mRepositoriesManager.startLive(file, roomId, liveType, liveName, isCompany, rtmp, hls, new CreateRoomCallback() {

            @Override
            public void onError(Call call, Exception e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(CreateRoomEntity response) {
                mILiveRoomView.setCreateRoomRes(response);

            }
        });
    }

    public void stopLive(String liveId, String isCompany) {
        mRepositoriesManager.stopLive(liveId, isCompany).subscribe(new SimpleObserver<StopRoomEntity>() {
            @Override
            public void onNext(StopRoomEntity stopRoomEntity) {
                mILiveRoomView.setStopLive(stopRoomEntity);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void getGift(String isCompany) {
        mRepositoriesManager.getGift(isCompany).subscribe(new SimpleObserver<GiftEntity>() {
            @Override
            public void onNext(GiftEntity gift) {
                mILiveRoomView.setGiftList(gift);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
