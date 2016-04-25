package com.futang.livestreaming.ui.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.app.component.AppProductionComponent;
import com.futang.livestreaming.widgets.CustomerProgress;

/**
 * Created by Administrator on 2016/4/7.
 */
public abstract class BaseActivity extends AppCompatActivity {


    private CustomerProgress mCustomerProgress;

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

    public void waittingDialog() {
        setTheme(android.R.style.Theme);
        mCustomerProgress = new CustomerProgress(this, "进行中,请稍后");
        mCustomerProgress.show();
    }

    public void stopCusDialog() {
        if (mCustomerProgress != null) {
            mCustomerProgress.dismiss();
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
