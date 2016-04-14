package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.activity.account.LoginActivity;
import com.futang.livestreaming.ui.module.LoginActivityModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/8.
 */
@ActivityScope
@Subcomponent(
        modules = LoginActivityModule.class
)
public interface LoginActivityComponent {

    LoginActivity inject(LoginActivity loginActivity);
}
