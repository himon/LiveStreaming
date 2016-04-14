package com.futang.livestreaming.ui.presenter;

import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.app.component.AppProductionComponent;
import com.futang.livestreaming.app.module.UserModule;
import com.futang.livestreaming.data.LiveManager;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.account.RegisterActivity;
import com.futang.livestreaming.ui.view.IRegisterView;
import com.futang.livestreaming.util.observer.SimpleObserver;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by Administrator on 2016/4/7.
 */
public class RegisterActivityPresenter {

    private IRegisterView mIRegisterView;
    private LiveManager mLiveManager;
    private final AppProductionComponent mAppProductionComponent;

    public RegisterActivityPresenter(IRegisterView iRegisterView) {
        this.mIRegisterView = iRegisterView;

        mAppProductionComponent = ((RegisterActivity) mIRegisterView).getAppProductionComponent();

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

    public void register(String phone, String loginPwd) {

        if (mLiveManager == null) return;

        Observable.combineLatest(
                Observable.from(mAppProductionComponent.userModuleFactory()),
                mLiveManager.register(phone, loginPwd),
                new Func2<UserModule.Factory, UserEntity, UserModule>() {
                    @Override
                    public UserModule call(UserModule.Factory factory, UserEntity userEntity) {
                        if (!"0".equals(userEntity.getCode())) {
                            return null;
                        }
                        return factory.create(userEntity);
                    }
                })
                .subscribe(new SimpleObserver<UserModule>() {

                    @Override
                    public void onNext(UserModule userModule) {
                        if (userModule != null) {
                            LiveApplication.get((RegisterActivity) mIRegisterView).createUserComponent(userModule);
                            mIRegisterView.toUserInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.print(e.getMessage());
                    }
                });
    }
}
