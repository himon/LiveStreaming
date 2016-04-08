package com.futang.livestreaming.app.module;

import com.futang.livestreaming.app.Scope.UserScope;
import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.data.api.LiveApi;
import com.futang.livestreaming.data.entity.UserEntity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/8.
 */
@Module
public class UserModule {

    private UserEntity mUser;
    private LiveApi mLiveApi;

    public UserModule(UserEntity user, LiveApi liveApi) {
        this.mUser = user;
        this.mLiveApi = liveApi;
    }

    @Provides
    @UserScope
    UserEntity provideUserEntity(){
        return mUser;
    }

    @Provides
    @UserScope
    RepositoriesManager provideRepositoriesManager() {
        return new RepositoriesManager(mUser, mLiveApi);
    }

    public static class Factory{
        private LiveApi mLiveApi;

        public Factory(LiveApi liveApi) {
            this.mLiveApi = liveApi;
        }

        public UserModule create(UserEntity user) {
            return new UserModule(user, mLiveApi);
        }
    }
}
