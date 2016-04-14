package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.activity.live.LivePublishActivity;
import com.futang.livestreaming.ui.module.LivePublishActivityModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/13.
 */
@ActivityScope
@Subcomponent(
        modules = LivePublishActivityModule.class
)
public interface LivePublishActivityComponent {

    LivePublishActivity inject(LivePublishActivity livePublishActivity);
}
