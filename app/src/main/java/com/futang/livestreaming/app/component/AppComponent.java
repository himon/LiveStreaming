package com.futang.livestreaming.app.component;

import android.app.Application;

import com.futang.livestreaming.app.module.AppModule;
import com.futang.livestreaming.app.module.UserModule;
import com.futang.livestreaming.ui.activity.account.RegisterActivity;
import com.futang.livestreaming.ui.activity.account.SplashActivity;
import com.futang.livestreaming.ui.component.LoginActivityComponent;
import com.futang.livestreaming.ui.component.RegisterActivityComponent;
import com.futang.livestreaming.ui.component.SplashActivityComponent;
import com.futang.livestreaming.ui.module.LoginActivityModule;
import com.futang.livestreaming.ui.module.RegisterActivityModule;
import com.futang.livestreaming.ui.module.SplashActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/7.
 */
@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {

    Application application();

    UserComponent plus(UserModule userModule);

    RegisterActivityComponent plus(RegisterActivityModule module);

    LoginActivityComponent plus(LoginActivityModule module);

    SplashActivityComponent plus(SplashActivityModule module);
}
