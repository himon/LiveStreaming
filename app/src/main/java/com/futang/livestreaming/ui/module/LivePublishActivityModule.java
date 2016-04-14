package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.presenter.LivePublishActivityPresenter;
import com.futang.livestreaming.ui.view.ILivePublishView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/13.
 */
@Module
public class LivePublishActivityModule {

    private ILivePublishView mILivePublishView;

    public LivePublishActivityModule(ILivePublishView iLivePublishView) {
        this.mILivePublishView = iLivePublishView;
    }

    @Provides
    @ActivityScope
    ILivePublishView provideLivePublishActivity() {
        return mILivePublishView;
    }

    @Provides
    @ActivityScope
    LivePublishActivityPresenter providLivePublishActivityPresenter(RepositoriesManager repositoriesManager) {
        return new LivePublishActivityPresenter(mILivePublishView, repositoriesManager);
    }
}
