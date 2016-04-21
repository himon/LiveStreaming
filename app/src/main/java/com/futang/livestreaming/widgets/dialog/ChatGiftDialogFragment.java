package com.futang.livestreaming.widgets.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.futang.livestreaming.R;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.entity.GiftEntity;
import com.futang.livestreaming.ui.fragment.ChatGiftFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lc on 16/4/19.
 */
public class ChatGiftDialogFragment extends DialogFragment {

    ViewPager mViewPager;
    TextView mTvMoney;
    TextView mTvRecharge;
    Button mBtnSend;

    private ArrayList<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ArrayList<GiftEntity.BodyBean> mGiftList;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = inflater.inflate(R.layout.dialog_fragment_chat_gift, container);
        initView();
        initData();
        return mView;
    }

    private void initView() {
        mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        mTvMoney = (TextView) mView.findViewById(R.id.tv_money);
        mTvRecharge = (TextView) mView.findViewById(R.id.tv_recharge);
        mBtnSend = (Button) mView.findViewById(R.id.btn_send);
    }

    private void initData() {
        Bundle bundle = getArguments();
        mGiftList = bundle.getParcelableArrayList(C.IntentKey.MESSAGE_INTENT_KEY);
        int size = mGiftList.size();

        int page = size % 8 == 0 ? size / 8 : size / 8 + 1;

        for (int i = 0; i < page; i++) {
            ChatGiftFragment fragment = new ChatGiftFragment();
            ArrayList<GiftEntity.BodyBean> list = new ArrayList<>();
            int end = 0;
            if (i == page - 1) {
                end = mGiftList.size();
            } else {
                end = 8 * i + 8;
            }

            for (int j = i * 8; j < end; j++) {
                list.add(mGiftList.get(j));
            }
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(C.IntentKey.MESSAGE_INTENT_KEY, list);
            fragment.setArguments(arguments);
            mTabs.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置Dialog在屏幕下端
        final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(layoutParams);

        //设置Dialog跟屏幕宽度一样宽
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);

        //设置Dialog背景色透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}