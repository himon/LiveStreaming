package com.futang.livestreaming.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.module.RegisterActivityModule;
import com.futang.livestreaming.ui.module.SplashActivityModule;
import com.futang.livestreaming.ui.presenter.SplashActivityPresenter;
import com.futang.livestreaming.ui.view.ISplashView;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

public class SplashActivity extends BaseActivity implements ISplashView, View.OnClickListener {

    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.btn_qq)
    ImageButton mBtnQQ;
    @Bind(R.id.btn_wechat)
    ImageButton mBtnWechat;
    private Platform platform;

    @Inject
    SplashActivityPresenter mPresenter;

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this)
                .getAppComponent()
                .plus(new SplashActivityModule(this))
                .inject(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //初始化SDK
        ShareSDK.initSDK(this);

    }

    @Override
    protected void setUpView() {
        initEvent();
    }

    @Override
    protected void setUpData() {

    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBtnQQ.setOnClickListener(this);
        mBtnWechat.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                toLogin();
                break;
            case R.id.btn_register:
                toRegister();
                break;
            case R.id.btn_qq:
                qq();
                break;
            case R.id.btn_wechat:
                break;
        }
    }

    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void qq() {
        platform = ShareSDK.getPlatform(this, QQ.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {
                    PlatformDb db = platform.getDb();
                    mPresenter.qqLogin(
                            db.getUserId(),
                            db.getUserName(),
                            db.getUserGender(),
                            db.getUserIcon(), "1", "0");

                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        platform.showUser(null);
    }

    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        if (platform != null && platform.isAuthValid()) {
            platform.removeAccount(true);
        }
    }

    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
