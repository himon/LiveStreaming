package com.futang.livestreaming.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.futang.livestreaming.R;
import com.futang.livestreaming.ui.base.BaseActivity;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

public class SplashActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.btn_qq)
    Button mBtnQQ;
    @Bind(R.id.btn_wechat)
    Button mBtnWechat;
    private Platform platform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //初始化SDK
        ShareSDK.initSDK(this);
        initEvent();
    }

    @Override
    protected void setupActivityComponent() {

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

    private void qq() {
        platform = ShareSDK.getPlatform(this, QQ.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {
                    platform.getDb().getUserId();
                    String id = res.get("id").toString();
                    String name=res.get("name").toString();//用户名
                    String description=res.get("description").toString();//描述
                    String profile_image_url=res.get("profile_image_url").toString();//头像链接
                    Message msg = new Message();
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
}
