package com.futang.livestreaming.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.module.RegisterActivityModule;
import com.futang.livestreaming.ui.presenter.RegisterActivityPresenter;
import com.futang.livestreaming.ui.view.IRegisterView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements IRegisterView, View.OnClickListener {

    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.btn_register)
    Button mBtnRegister;

    @Inject
    RegisterActivityPresenter mPresetner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initEvent();
    }

    private void initEvent() {
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this)
                .getAppComponent()
                .plus(new RegisterActivityModule(this))
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                mPresetner.register(mEtPhone.getText().toString(), mEtPassword.getText().toString());
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
