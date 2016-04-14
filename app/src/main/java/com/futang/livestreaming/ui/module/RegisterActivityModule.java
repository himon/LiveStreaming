package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.presenter.RegisterActivityPresenter;
import com.futang.livestreaming.ui.view.IRegisterView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/7.
 */
@Module
public class RegisterActivityModule {

    private IRegisterView mIRegisterView;

    public RegisterActivityModule(IRegisterView iRegisterView) {
        this.mIRegisterView = iRegisterView;
    }

    @Provides
    @ActivityScope
    IRegisterView provideRegisterActivity() {
        return mIRegisterView;
    }

    @Provides
    @ActivityScope
    RegisterActivityPresenter provideRegisterActivityPresenter() {
        return new RegisterActivityPresenter(mIRegisterView);
    }
}
