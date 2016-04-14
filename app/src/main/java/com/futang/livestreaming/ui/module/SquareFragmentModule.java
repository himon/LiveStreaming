package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.presenter.MainActivityPresenter;
import com.futang.livestreaming.ui.presenter.SquareFragmentPresenter;
import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.view.ISquareFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/14.
 */
@Module
public class SquareFragmentModule {

    private ISquareFragmentView mISquareFragmentView;

    public SquareFragmentModule(ISquareFragmentView iSquareFragmentView) {
        this.mISquareFragmentView = iSquareFragmentView;
    }

    @Provides
    @ActivityScope
    ISquareFragmentView provideSquareFragment() {
        return mISquareFragmentView;
    }

    @Provides
    @ActivityScope
    SquareFragmentPresenter provideRepositoriesSquareFragmentPresenter(RepositoriesManager repositoriesManager) {
        return new SquareFragmentPresenter(mISquareFragmentView, repositoriesManager);
    }
}
