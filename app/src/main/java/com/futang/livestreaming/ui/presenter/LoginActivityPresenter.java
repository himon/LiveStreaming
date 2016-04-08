package com.futang.livestreaming.ui.presenter;

import com.futang.livestreaming.app.component.AppProductionComponent;
import com.futang.livestreaming.data.LiveManager;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.account.LoginActivity;
import com.futang.livestreaming.ui.view.ILoginView;
import com.futang.livestreaming.util.Observer.SimpleObserver;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/8.
 */
public class LoginActivityPresenter {

    private ILoginView mILoginView;
    private LiveManager mLiveManager;

    public LoginActivityPresenter(ILoginView iLoginView) {
        this.mILoginView = iLoginView;

        AppProductionComponent appProductionComponent = ((LoginActivity) mILoginView).getAppProductionComponent();
        Futures.addCallback(appProductionComponent.liveManager(), new FutureCallback<LiveManager>() {
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

        mLiveManager.login(phone, loginPwd)
                .subscribe(new SimpleObserver<UserEntity>() {
            @Override
            public void onNext(UserEntity userEntity) {
                UserEntity user = userEntity;
            }

            @Override
            public void onError(Throwable e) {
                System.out.print(e.getMessage());
            }
        });
    }
}
