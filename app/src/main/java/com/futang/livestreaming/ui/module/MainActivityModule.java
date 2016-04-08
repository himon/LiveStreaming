package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.Scope.ActivityScope;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.presenter.MainActivityPresenter;
import com.futang.livestreaming.ui.view.IMainView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/8.
 */
@Module
public class MainActivityModule {

    private IMainView mIMainView;

    public MainActivityModule(IMainView iMainView) {
        this.mIMainView = iMainView;
    }

    @Provides
    @ActivityScope
    IMainView provideMainActivity() {
        return mIMainView;
    }

    @Provides
    @ActivityScope
    MainActivityPresenter provideRepositoriesListActivityPresenter(RepositoriesManager repositoriesManager) {
        return new MainActivityPresenter(mIMainView, repositoriesManager);
    }
}
