package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.scope.ActivityScope;
import com.futang.livestreaming.ui.activity.account.SplashActivity;
import com.futang.livestreaming.ui.module.SplashActivityModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/11.
 */
@ActivityScope
@Subcomponent(
        modules = SplashActivityModule.class
)
public interface SplashActivityComponent {

    SplashActivity inject(SplashActivity splashActivity);
}
