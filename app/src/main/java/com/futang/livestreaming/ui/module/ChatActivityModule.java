package com.futang.livestreaming.ui.module;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.ui.presenter.ChatActivityPresenter;
import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.view.IChatView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/16.
 */
@Module
public class ChatActivityModule {

    private IChatView mIChatView;

    public ChatActivityModule(IChatView IChatView) {
        this.mIChatView = IChatView;
    }

    @Provides
    @ActivityScope
    IChatView provideChatActivity() {
        return mIChatView;
    }

    @Provides
    @ActivityScope
    ChatActivityPresenter providChatActivityPresenter(RepositoriesManager repositoriesManager) {
        return new ChatActivityPresenter(mIChatView, repositoriesManager);
    }

}
