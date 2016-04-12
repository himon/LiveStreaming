package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.Scope.ActivityScope;
import com.futang.livestreaming.ui.presenter.RegisterNextActivityPresenter;
import com.futang.livestreaming.ui.view.IRegisterNextView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/12.
 */
@Module
public class RegisterNextActivityModule {

    private IRegisterNextView mIRegisterNextView;

    public RegisterNextActivityModule(IRegisterNextView iRegisterNextView) {
        this.mIRegisterNextView = iRegisterNextView;
    }

    @Provides
    @ActivityScope
    IRegisterNextView provideRegisterNextActivity() {
        return mIRegisterNextView;
    }

    @Provides
    @ActivityScope
    RegisterNextActivityPresenter provideRegisterNextActivityPresenter(RepositoriesManager repositoriesManager) {
        return new RegisterNextActivityPresenter(mIRegisterNextView, repositoriesManager);
    }
}
