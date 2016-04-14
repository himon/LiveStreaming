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

    public void getRoomList(String liveType, String isCompany, int page) {
        mRepositoriesManager.getRoomList(liveType, isCompany, page + "").subscribe(new SimpleObserver<RoomEntity>(){
            @Override
            public void onNext(RoomEntity room) {
                mISquareFragmentView.setRoomDate(room);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
