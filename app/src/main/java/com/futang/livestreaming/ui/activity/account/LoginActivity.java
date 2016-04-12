package com.futang.livestreaming.ui.activity.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.base.ToolbarActivity;
import com.futang.livestreaming.ui.module.LoginActivityModule;
import com.futang.livestreaming.ui.module.RegisterActivityModule;
import com.futang.livestreaming.ui.presenter.LoginActivityPresenter;
import com.futang.livestreaming.ui.view.ILoginView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends ToolbarActivity implements ILoginView, View.OnClickListener {

    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.btn_login)
    Button mBtnLogin;

    @Inject
    LoginActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login, R.string.title_login, MODE_BACK);
        ButterKnife.bind(this);
    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this)
                .getAppComponent()
                .plus(new LoginActivityModule(this))
                .inject(this);
    }

    @Override
    protected void setUpView() {
        initEvent();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mPresenter.login(mEtPhone.getText().toString(), mEtPassword.getText().toString());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, RegisterNextActivity.class);
        startActivity(intent);
    }
}
