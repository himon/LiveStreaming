package com.futang.livestreaming.ui.presenter;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.view.ILivePublishView;
import com.futang.livestreaming.util.callback.CreateRoomCallback;
import com.futang.livestreaming.util.callback.UserCallback;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/13.
 */
public class LivePublishActivityPresenter {

    private ILivePublishView mILivePublishView;
    private RepositoriesManager mRepositoriesManager;

    public RepositoriesManager getmRepositoriesManager() {
        return mRepositoriesManager;
    }

    public LivePublishActivityPresenter(ILivePublishView iLivePublishView, RepositoriesManager repositoriesManager) {
        this.mILivePublishView = iLivePublishView;
        this.mRepositoriesManager = repositoriesManager;
    }

    public void startLive(File file, String roomId, String liveType, String liveName, String isCompany) {
        mRepositoriesManager.startLive(file, roomId, liveType, liveName, isCompany, new CreateRoomCallback(){

            @Override
            public void onError(Call call, Exception e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(CreateRoomEntity response) {
                mILivePublishView.setCreateRoomRes(response);

            }
        });
    }
}
