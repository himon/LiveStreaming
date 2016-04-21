package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.activity.live.LiveRoomActivity;
import com.futang.livestreaming.ui.module.LiveRoomActivityModule;
import com.futang.livestreaming.ui.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/16.
 */
@ActivityScope
@Subcomponent(
        modules = LiveRoomActivityModule.class
)
public interface LiveRoomActivityComponent {

    LiveRoomActivity inject(LiveRoomActivity liveRoomActivity);
}
