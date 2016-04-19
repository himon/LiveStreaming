package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.activity.live.ChatActivity;
import com.futang.livestreaming.ui.module.ChatActivityModule;
import com.futang.livestreaming.ui.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/16.
 */
@ActivityScope
@Subcomponent(
        modules = ChatActivityModule.class
)
public interface ChatActivityComponent {

    ChatActivity inject(ChatActivity chatActivity);
}
