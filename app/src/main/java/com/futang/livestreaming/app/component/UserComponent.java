package com.futang.livestreaming.app.component;

import com.futang.livestreaming.app.Scope.UserScope;
import com.futang.livestreaming.app.module.UserModule;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.component.MainActivityComponent;
import com.futang.livestreaming.ui.module.MainActivityModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016/4/8.
 */
@UserScope
@Subcomponent(
        modules = {
                UserModule.class
        }
)
public interface UserComponent {

        MainActivityComponent plus(MainActivityModule module);
}
