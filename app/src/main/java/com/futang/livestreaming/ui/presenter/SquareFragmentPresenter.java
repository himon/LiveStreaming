package com.futang.livestreaming.ui.presenter;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.RoomEntity;
import com.futang.livestreaming.ui.view.ISquareFragmentView;
import com.futang.livestreaming.util.observer.SimpleObserver;

/**
 * Created by Administrator on 2016/4/14.
 */
public class SquareFragmentPresenter {

    private ISquareFragmentView mISquareFragmentView;
    private RepositoriesManager mRepositoriesManager;

    public SquareFragmentPresenter(ISquareFragmentView iSquareFragmentView, RepositoriesManager repositoriesManager) {
        this.mISquareFragmentView = iSquareFragmentView;
        this.mRepositoriesManager = repositoriesManager;
    }
}
