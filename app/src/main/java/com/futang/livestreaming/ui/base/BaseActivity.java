package com.futang.livestreaming.ui.base;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.app.component.AppProductionComponent;

/**
 * Created by Administrator on 2016/4/7.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
        setUpContentView();
        setUpView();
        setUpData();
    }

    protected abstract void setupActivityComponent();

    protected abstract void setUpContentView();

    protected abstract void setUpView();

    protected abstract void setUpData();

    public AppProductionComponent getAppProductionComponent() {
        return LiveApplication.get(this).getAppProductionComponent();
    }
}
