package com.futang.livestreaming.ui.component;

import com.futang.livestreaming.ui.fragment.SquareFragment;
import com.futang.livestreaming.ui.module.MainActivityModule;
import com.futang.livestreaming.ui.module.SquareFragmentModule;
import com.futang.livestreaming.ui.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/14.
 */
@ActivityScope
@Subcomponent(
        modules = SquareFragmentModule.class
)
public interface SquareFragmentComponent {

    SquareFragment inject(SquareFragment squareFragment);
}
