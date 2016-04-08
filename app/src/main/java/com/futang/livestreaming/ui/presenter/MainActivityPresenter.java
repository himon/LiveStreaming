package com.futang.livestreaming.ui.presenter;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.view.IMainView;

/**
 * Created by Administrator on 2016/4/8.
 */
public class MainActivityPresenter {

    private IMainView mIMainView;
    private RepositoriesManager mRepositoriesManager;

    public MainActivityPresenter(IMainView iMainView, RepositoriesManager repositoriesManager) {
        this.mIMainView = iMainView;
        this.mRepositoriesManager = repositoriesManager;
    }
}
