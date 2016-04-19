package com.futang.livestreaming.widgets.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.futang.livestreaming.R;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.entity.GiftEntity;
import com.futang.livestreaming.ui.activity.live.ChatActivity;
import com.futang.livestreaming.ui.fragment.BlankFragment;
import com.futang.livestreaming.ui.fragment.ChatGiftFragment;
import com.futang.livestreaming.ui.fragment.SquareHotFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class ChatPresenterPopupWindow extends PopupWindow {

    private ChatActivity mContext;

    private ViewPager mViewPager;

    private List<GiftEntity.BodyBean> mGiftList;
    private FragmentPagerAdapter mAdapter;
    private ArrayList<Fragment> mTabs = new ArrayList<>();

    public ChatPresenterPopupWindow(ChatActivity context, View parent, List<GiftEntity.BodyBean> giftList) {
        mContext = context;
        mGiftList = giftList;
        View view = View.inflate(context, R.layout.window_chat_presenter, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_1));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(false);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {

        int page = mGiftList.size() % 8 == 0 ? mGiftList.size() / 8 : mGiftList.size() / 8 + 1;

         mAdapter = new FragmentPagerAdapter(mContext.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };

        for (int i = 0; i < page; i++) {
            BlankFragment fragment = new BlankFragment();
            int end;
            ArrayList<GiftEntity.BodyBean> list = new ArrayList<>();
            if (i == page - 1) {
                end = mGiftList.size();
            } else {
                end = i * 8 + 8;
            }
            for (int j = i * 8; j < end; j++) {
                list.add(mGiftList.get(j));
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(C.IntentKey.MESSAGE_INTENT_KEY, list);
            fragment.setArguments(bundle);
            mTabs.add(fragment);
        }
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(page);
        mViewPager.setCurrentItem(1);
    }


}
