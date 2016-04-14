package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.activity.account.RegisterActivity;
import com.futang.livestreaming.ui.module.RegisterActivityModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/7.
 */
@ActivityScope
@Subcomponent(
        modules = RegisterActivityModule.class
)
public interface RegisterActivityComponent {

    RegisterActivity inject(RegisterActivity registerActivity);
}
