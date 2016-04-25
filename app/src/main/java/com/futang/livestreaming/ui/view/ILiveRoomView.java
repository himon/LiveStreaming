package com.futang.livestreaming.ui.view;

import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.GiftEntity;
import com.futang.livestreaming.data.entity.StopRoomEntity;
import com.futang.livestreaming.data.entity.WatcherPicEntity;

/**
 * Created by Administrator on 2016/4/16.
 */
public interface ILiveRoomView {
    void setCreateRoomRes(CreateRoomEntity response);

    void setStopLive(StopRoomEntity stopRoomEntity);

    void setGiftList(GiftEntity gift);

    void setWatcherList(WatcherPicEntity watcherPicEntity);
}
