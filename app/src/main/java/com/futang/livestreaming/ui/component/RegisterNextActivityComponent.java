package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.activity.account.RegisterNextActivity;
import com.futang.livestreaming.ui.module.RegisterNextActivityModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/12.
 */
@ActivityScope
@Subcomponent(
        modules = RegisterNextActivityModule.class
)
public interface RegisterNextActivityComponent {

    RegisterNextActivity inject(RegisterNextActivity registerNextActivity);
}
