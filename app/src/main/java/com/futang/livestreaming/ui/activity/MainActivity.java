package com.futang.livestreaming.ui.activity;

import android.os.Bundle;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.module.MainActivityModule;
import com.futang.livestreaming.ui.view.IMainView;

public class MainActivity extends BaseActivity implements IMainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this).getUserComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }
}
