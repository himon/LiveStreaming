package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.Scope.ActivityScope;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.module.MainActivityModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/8.
 */
@ActivityScope
@Subcomponent(
        modules = MainActivityModule.class
)
public interface MainActivityComponent {

    MainActivity inject(MainActivity mainActivity);
}
