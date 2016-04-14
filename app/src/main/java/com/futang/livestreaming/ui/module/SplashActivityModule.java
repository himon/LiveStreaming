package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.presenter.SplashActivityPresenter;
import com.futang.livestreaming.ui.view.ISplashView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/11.
 */
@Module
public class SplashActivityModule {

    private ISplashView mISplashView;

    public SplashActivityModule(ISplashView iSplashView) {
        this.mISplashView = iSplashView;
    }

    @Provides
    @ActivityScope
    ISplashView provideSplashActivity() {
        return mISplashView;
    }

    @Provides
    @ActivityScope
    SplashActivityPresenter providSplashActivityPresenter() {
        return new SplashActivityPresenter(mISplashView);
    }
}
