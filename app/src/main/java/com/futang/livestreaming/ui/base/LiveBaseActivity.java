package com.futang.livestreaming.ui.base;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.futang.livestreaming.ui.base.BaseActivity;

import butterknife.ButterKnife;


/**
 * des: activity 基类
 */
public abstract class LiveBaseActivity extends BaseActivity {

    /**
     * 获取内容页面的布局.
     *
     * @return 返回内容页面的布局
     */
    protected abstract int getContentViewLayout();

    /**
     * 初始化从外部传递过来的数据.
     */
    protected abstract void initExtraData();

    /**
     * 初始化子类中的变量.
     */
    protected abstract void initVariables();


    /**
     * 获取页面的标签, 即子类的名称， 必须返回.
     *
     * @return 返回页面的标签
     */
    protected abstract String getPageTag();
}
