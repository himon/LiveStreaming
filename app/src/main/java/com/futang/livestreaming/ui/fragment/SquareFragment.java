package com.futang.livestreaming.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futang.livestreaming.R;
import com.futang.livestreaming.data.entity.RoomEntity;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.base.BaseFragment;
import com.futang.livestreaming.ui.base.BaseListFragment;
import com.futang.livestreaming.ui.module.SquareFragmentModule;
import com.futang.livestreaming.ui.presenter.SquareFragmentPresenter;
import com.futang.livestreaming.ui.view.ISquareFragmentView;
import com.futang.livestreaming.widgets.pull.BaseViewHolder;
import com.futang.livestreaming.widgets.pull.PullRecycler;
import com.futang.livestreaming.widgets.pull.layoutmanager.ILayoutManager;
import com.futang.livestreaming.widgets.pull.layoutmanager.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SquareFragment extends BaseFragment implements ISquareFragmentView {

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Inject
    SquareFragmentPresenter mPresenter;

    private int page = 0;

    public SquareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square, container, false);
        ButterKnife.bind(this, view);

        setUpView();
        return view;
    }

    private void setUpView() {
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new SquareHotFragment(), "热门");
        adapter.addFragment(new SquareHotFragment(), "推荐");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void setupActivityComponent() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getmMainActivityComponent().plus(new SquareFragmentModule(this)).inject(this);
        }
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
