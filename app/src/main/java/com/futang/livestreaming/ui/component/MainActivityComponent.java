package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.module.SquareFragmentModule;
import com.futang.livestreaming.ui.module.SquareHotFragmentModule;
import com.futang.livestreaming.ui.scope.ActivityScope;
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

    SquareFragmentComponent plus(SquareFragmentModule module);

    SquareHotFragmentComponent plus(SquareHotFragmentModule module);
}
