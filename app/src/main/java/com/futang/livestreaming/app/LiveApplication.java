package com.futang.livestreaming.app;

import android.app.Application;
import android.content.Context;

import com.futang.livestreaming.app.component.AppComponent;
import com.futang.livestreaming.app.component.AppProductionComponent;
import com.futang.livestreaming.app.component.DaggerAppComponent;
import com.futang.livestreaming.app.component.DaggerAppProductionComponent;
import com.futang.livestreaming.app.component.UserComponent;
import com.futang.livestreaming.app.module.AppModule;
import com.futang.livestreaming.app.module.UserModule;

import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/7.
 */
public class LiveApplication extends Application {

    private AppComponent mAppComponent;
    private AppProductionComponent mAppProductionComponent;
    private UserComponent mUserComponent;

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public AppProductionComponent getAppProductionComponent() {
        return mAppProductionComponent;
    }

    public UserComponent getUserComponent() {
        return mUserComponent;
    }

    public static LiveApplication get(Context context) {
        return (LiveApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
    }

    private void initAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        mAppProductionComponent = DaggerAppProductionComponent.builder()
                .executor(Executors.newSingleThreadExecutor())
                .appComponent(mAppComponent)
                .build();
    }

    public UserComponent createUserComponent(UserModule userModule) {
        mUserComponent = mAppComponent.plus(userModule);
        return mUserComponent;
    }
}
