package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.presenter.SquareFragmentPresenter;
import com.futang.livestreaming.ui.presenter.SquareHotFragmentPresenter;
import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.view.ISquareHotFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/14.
 */
@Module
public class SquareHotFragmentModule {

    private ISquareHotFragmentView mISquareHotFragmentView;

    public SquareHotFragmentModule(ISquareHotFragmentView iSquareHotFragmentView) {
        this.mISquareHotFragmentView = iSquareHotFragmentView;
    }

    @Provides
    @ActivityScope
    ISquareHotFragmentView provideSquareHotFragment() {
        return mISquareHotFragmentView;
    }

    @Provides
    @ActivityScope
    SquareHotFragmentPresenter provideRepositoriesSquareHotFragmentPresenter(RepositoriesManager repositoriesManager) {
        return new SquareHotFragmentPresenter(mISquareHotFragmentView, repositoriesManager);
    }
}
