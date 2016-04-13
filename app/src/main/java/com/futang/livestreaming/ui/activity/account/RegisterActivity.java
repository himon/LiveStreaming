package com.futang.livestreaming.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.base.ToolbarActivity;
import com.futang.livestreaming.ui.module.RegisterActivityModule;
import com.futang.livestreaming.ui.presenter.RegisterActivityPresenter;
import com.futang.livestreaming.ui.view.IRegisterView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends ToolbarActivity implements IRegisterView, View.OnClickListener {

    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.iv_phone_delete)
    ImageView mIvPhoneDel;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.iv_password_delete)
    ImageView mIvPasswordDel;
    @Bind(R.id.btn_get_code)
    Button mBtnGetCode;
    @Bind(R.id.btn_register)
    Button mBtnRegister;

    @Inject
    RegisterActivityPresenter mPresetner;

    private void initEvent() {
        mBtnRegister.setOnClickListener(this);

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    mIvPhoneDel.setVisibility(View.VISIBLE);
                }else{
                    mIvPhoneDel.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    mIvPasswordDel.setVisibility(View.VISIBLE);
                }else{
                    mIvPasswordDel.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this)
                .getAppComponent()
                .plus(new RegisterActivityModule(this))
                .inject(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_register, R.string.title_register, MODE_BACK);
        ButterKnife.bind(this);
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
    public void toUserInfo() {
        Intent intent = new Intent(this, RegisterNextActivity.class);
        startActivity(intent);
    }
}
