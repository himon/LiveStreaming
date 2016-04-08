package com.futang.livestreaming.app.component;

import com.futang.livestreaming.app.module.ApiProducerModule;
import com.futang.livestreaming.app.module.UserModule;
import com.futang.livestreaming.data.LiveManager;
import com.google.common.util.concurrent.ListenableFuture;

import dagger.producers.ProducerModule;
import dagger.producers.ProductionComponent;

/**
 * Created by Administrator on 2016/4/7.
 */
@ProductionComponent(
        dependencies = AppComponent.class,
        modules = ApiProducerModule.class
)
public interface AppProductionComponent {

    ListenableFuture<LiveManager> liveManager();

    ListenableFuture<UserModule.Factory> userModuleFactory();
}
