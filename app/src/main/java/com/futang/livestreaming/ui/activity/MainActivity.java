package com.futang.livestreaming.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.account.RegisterNextActivity;
import com.futang.livestreaming.ui.activity.live.ChatActivity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.base.ToolbarActivity;
import com.futang.livestreaming.ui.component.MainActivityComponent;
import com.futang.livestreaming.ui.fragment.InterestFragment;
import com.futang.livestreaming.ui.fragment.MineFragment;
import com.futang.livestreaming.ui.fragment.ShopFragment;
import com.futang.livestreaming.ui.fragment.SquareFragment;
import com.futang.livestreaming.ui.module.MainActivityModule;
import com.futang.livestreaming.ui.presenter.MainActivityPresenter;
import com.futang.livestreaming.ui.view.IMainView;
import com.futang.livestreaming.widgets.CircleTransform;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends ToolbarActivity implements IMainView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.frame_main)
    FrameLayout mFlMain;
    @Bind(R.id.rg_menu)
    RadioGroup mRgMenu;
    @Bind(R.id.rb_square)
    RadioButton mRbSquare;
    @Bind(R.id.rb_interest)
    RadioButton mRbInterest;
    @Bind(R.id.rb_shop)
    RadioButton mRbShop;
    @Bind(R.id.rb_mine)
    RadioButton mRbMine;
    @Bind(R.id.iv_live)
    ImageView mIvLive;

    @Inject
    MainActivityPresenter mPresenter;

    private FragmentManager mFragmentManager;
    private SquareFragment mSquareFragment;
    private InterestFragment mInterestFragment;
    private ShopFragment mShopFragment;
    private MineFragment mMineFragment;
    private MainActivityComponent mMainActivityComponent;
    private UserEntity.BodyBean mUser;
    private int mChannelId;

    public MainActivityComponent getmMainActivityComponent() {
        return mMainActivityComponent;
    }

    @Override
    protected void setupActivityComponent() {
        mMainActivityComponent = LiveApplication.get(this).getUserComponent()
                .plus(new MainActivityModule(this));
        mMainActivityComponent.inject(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_main, R.string.title_square, R.menu.menu_home, MODE_HOME);
        ButterKnife.bind(this);
        setupAVKit();
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void setUpView() {
        mRgMenu.setOnCheckedChangeListener(this);
        mRbSquare.setChecked(true);
        initUserIcon();
        initEvent();
    }

    private void initUserIcon() {
        String picture = mPresenter.getmRepositoriesManager().getmUser().getBody().getPicture();
        Glide.with(this)
                .load(picture)
                .centerCrop()
                .placeholder(android.R.color.white)
                .crossFade()
                .transform(new CircleTransform(this))
                .into(toolbar_icon);
        toolbar_icon.setVisibility(View.VISIBLE);
        toolbar_icon.setOnClickListener(this);
    }

    private void initEvent() {
        mIvLive.setOnClickListener(this);
    }

    @Override
    protected void setUpData() {
        mUser = mPresenter.getmRepositoriesManager().getmUser().getBody();
        mChannelId = Integer.valueOf(mUser.getId()) + 1;
    }

    private void setupAVKit() {
        //ZegoApi.setupAVKit(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_live:
                ChatActivity.actionStart(MainActivity.this, mChannelId, 0);
                break;
            case R.id.toolbar_icon:

                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTran = mFragmentManager
                .beginTransaction();
        hideFragments(fragmentTran);
        switch (checkedId) {
            case R.id.rb_square:
                if (mSquareFragment == null) {
                    mSquareFragment = new SquareFragment();
                    fragmentTran.add(R.id.frame_main, mSquareFragment);
                }
                fragmentTran.show(mSquareFragment);
                break;
            case R.id.rb_interest:
                if (mInterestFragment == null) {
                    mInterestFragment = new InterestFragment();
                    fragmentTran.add(R.id.frame_main, mInterestFragment);
                }
                fragmentTran.show(mInterestFragment);
                break;
            case R.id.rb_shop:
                if (mShopFragment == null) {
                    mShopFragment = new ShopFragment();
                    fragmentTran.add(R.id.frame_main, mShopFragment);
                }
                fragmentTran.show(mShopFragment);
                break;
            case R.id.rb_mine:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTran.add(R.id.frame_main, mMineFragment);
                }
                fragmentTran.show(mMineFragment);
                break;
        }
        fragmentTran.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mSquareFragment != null) {
            transaction.hide(mSquareFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
        if (mShopFragment != null) {
            transaction.hide(mShopFragment);
        }
        if (mInterestFragment != null) {
            transaction.hide(mInterestFragment);
        }
    }
}
