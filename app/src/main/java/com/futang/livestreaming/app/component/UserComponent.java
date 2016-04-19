package com.futang.livestreaming.app.component;

import com.futang.livestreaming.app.scope.UserScope;
import com.futang.livestreaming.app.module.UserModule;
import com.futang.livestreaming.ui.component.ChatActivityComponent;
import com.futang.livestreaming.ui.component.MainActivityComponent;
import com.futang.livestreaming.ui.component.RegisterNextActivityComponent;
import com.futang.livestreaming.ui.module.ChatActivityModule;
import com.futang.livestreaming.ui.module.MainActivityModule;
import com.futang.livestreaming.ui.module.RegisterNextActivityModule;

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

        RegisterNextActivityComponent plus(RegisterNextActivityModule module);

        ChatActivityComponent plus(ChatActivityModule module);
}
