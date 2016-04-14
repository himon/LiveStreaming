package com.futang.livestreaming.ui.presenter;

import android.text.TextUtils;

import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.app.component.AppProductionComponent;
import com.futang.livestreaming.app.module.UserModule;
import com.futang.livestreaming.data.LiveManager;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.account.LoginActivity;
import com.futang.livestreaming.ui.view.ILoginView;
import com.futang.livestreaming.util.observer.SimpleObserver;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by Administrator on 2016/4/8.
 */
public class LoginActivityPresenter {

    private ILoginView mILoginView;
    private LiveManager mLiveManager;
    private final AppProductionComponent mAppProductionComponent;
    private UserEntity.BodyBean mUser;

    public LoginActivityPresenter(ILoginView iLoginView) {
        this.mILoginView = iLoginView;

        mAppProductionComponent = ((LoginActivity) mILoginView).getAppProductionComponent();
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

    public void login(String phone, String loginPwd) {

        if (mLiveManager == null) return;

        Observable.combineLatest(
                Observable.from(mAppProductionComponent.userModuleFactory()),
                mLiveManager.login(phone, loginPwd),
                new Func2<UserModule.Factory, UserEntity, UserModule>() {
                    @Override
                    public UserModule call(UserModule.Factory factory, UserEntity userEntity) {
                        if (!"0".equals(userEntity.getCode())) {
                            return null;
                        }
                        mUser = userEntity.getBody();
                        return factory.create(userEntity);
                    }
                })
                .subscribe(new SimpleObserver<UserModule>() {

                    @Override
                    public void onNext(UserModule userModule) {
                        if (userModule != null) {
                            LiveApplication.get((LoginActivity) mILoginView).createUserComponent(userModule);
                            if(mUser != null && !TextUtils.isEmpty(mUser.getUserName())){
                                mILoginView.toMainActivity();
                            }else {
                                mILoginView.toEditUserInfo();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.print(e.getMessage());
                    }
                });
    }


}
