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
import com.futang.livestreaming.util.loader.GlideImageLoader;
import com.futang.livestreaming.util.loader.GlidePauseOnScrollListener;

import java.util.concurrent.Executors;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

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
        initGalleryFinal();
    }

    private void initGalleryFinal() {
        //建议在application中配置
        //设置主题
        ThemeConfig theme = ThemeConfig.DEFAULT;
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)//显示相机
                .setEnableEdit(true)//图片可编辑
                .setEnableCrop(true) //裁剪
                .setEnableRotate(true)//旋转
                .setCropSquare(true)//裁剪成正方形
                .setEnablePreview(true) //启动预览
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
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
