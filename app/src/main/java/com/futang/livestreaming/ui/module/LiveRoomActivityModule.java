package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.presenter.LiveRoomActivityPresenter;
import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.view.ILiveRoomView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/16.
 */
@Module
public class LiveRoomActivityModule {

    private ILiveRoomView mILiveRoomView;

    public LiveRoomActivityModule(ILiveRoomView iLiveRoomView) {
        this.mILiveRoomView = iLiveRoomView;
    }

    @Provides
    @ActivityScope
    ILiveRoomView provideLiveRoomActivity() {
        return mILiveRoomView;
    }

    @Provides
    @ActivityScope
    LiveRoomActivityPresenter providLiveRoomActivityPresenter(RepositoriesManager repositoriesManager) {
        return new LiveRoomActivityPresenter(mILiveRoomView, repositoriesManager);
    }

}
