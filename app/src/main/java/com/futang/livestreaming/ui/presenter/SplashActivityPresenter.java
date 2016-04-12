package com.futang.livestreaming.ui.presenter;

import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.app.component.AppProductionComponent;
import com.futang.livestreaming.app.module.UserModule;
import com.futang.livestreaming.data.LiveManager;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.account.LoginActivity;
import com.futang.livestreaming.ui.activity.account.RegisterActivity;
import com.futang.livestreaming.ui.activity.account.SplashActivity;
import com.futang.livestreaming.ui.view.ISplashView;
import com.futang.livestreaming.util.Observer.SimpleObserver;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by Administrator on 2016/4/11.
 */
public class SplashActivityPresenter {

    private ISplashView mISplashView;
    private LiveManager mLiveManager;
    private AppProductionComponent mAppProductionComponent;

    public SplashActivityPresenter(ISplashView iSplashView) {

        this.mISplashView = iSplashView;
        mAppProductionComponent = ((SplashActivity) mISplashView).getAppProductionComponent();
        Futures.addCallback(mAppProductionComponent.liveManager(), new FutureCallback<LiveManager>() {
            @Override
            public void onSuccess(LiveManager result) {
                mLiveManager = result;
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void qqLogin(String qq, String userName, String sex, String ico, String loginType, String isCompany) {

        Observable.combineLatest(
                Observable.from(mAppProductionComponent.userModuleFactory()),
                mLiveManager.quickLogin(qq, userName, sex, ico, loginType, isCompany),
                new Func2<UserModule.Factory, UserEntity, UserModule>() {
                    @Override
                    public UserModule call(UserModule.Factory factory, UserEntity userEntity) {
                        return factory.create(userEntity);
                    }
                })
                .subscribe(new SimpleObserver<UserModule>() {

                    @Override
                    public void onNext(UserModule userModule) {
                        LiveApplication.get((SplashActivity) mISplashView).createUserComponent(userModule);
                        mISplashView.toMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.print(e.getMessage());
                    }
                });
    }
}
