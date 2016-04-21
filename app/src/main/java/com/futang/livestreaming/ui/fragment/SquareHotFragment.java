package com.futang.livestreaming.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futang.livestreaming.R;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.entity.RoomEntity;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.activity.live.LiveRoomActivity;
import com.futang.livestreaming.ui.base.BaseListFragment;
import com.futang.livestreaming.ui.module.SquareFragmentModule;
import com.futang.livestreaming.ui.module.SquareHotFragmentModule;
import com.futang.livestreaming.ui.presenter.SquareFragmentPresenter;
import com.futang.livestreaming.ui.presenter.SquareHotFragmentPresenter;
import com.futang.livestreaming.ui.view.ISquareFragmentView;
import com.futang.livestreaming.ui.view.ISquareHotFragmentView;
import com.futang.livestreaming.util.ToastUtils;
import com.futang.livestreaming.widgets.CircleTransform;
import com.futang.livestreaming.widgets.pull.BaseViewHolder;
import com.futang.livestreaming.widgets.pull.PullRecycler;
import com.futang.livestreaming.widgets.pull.layoutmanager.ILayoutManager;
import com.futang.livestreaming.widgets.pull.layoutmanager.MyLinearLayoutManager;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SquareHotFragment extends BaseListFragment<RoomEntity.BodyBean> implements ISquareHotFragmentView {


    @Inject
    SquareHotFragmentPresenter mPresenter;

    private int page = 0;

    public SquareHotFragment() {
        // Required empty public constructor
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_square_list_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler.setRefreshing();
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getContext());
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return super.getItemDecoration();
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
            page = 0;
        }
        mPresenter.getRoomList("0", "0", page);
        page++;
    }

    @Override
    protected void setupActivityComponent() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getmMainActivityComponent().plus(new SquareHotFragmentModule(this)).inject(this);
        }
    }

    @Override
    public void setRoomDate(RoomEntity room) {
        mDataList.addAll(room.getBody());
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
        if (mDataList.size() < 100) {
            recycler.enableLoadMore(true);
        } else {
            recycler.enableLoadMore(false);
        }
    }

    class SampleViewHolder extends BaseViewHolder {

        ImageView mIvUserIcon;
        TextView mTvUserName;
        TextView mTvCount;
        ImageView mIvRoomCover;

        public SampleViewHolder(View itemView) {
            super(itemView);
            mIvUserIcon = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            mTvCount = (TextView) itemView.findViewById(R.id.tv_count);
            mIvRoomCover = (ImageView) itemView.findViewById(R.id.iv_room_cover);
        }

        @Override
        public void onBindViewHolder(int position) {

            RoomEntity.BodyBean room = mDataList.get(position);

            Glide.with(mIvUserIcon.getContext())
                    .load(room.getUserPicture())
                    .centerCrop()
                    .placeholder(android.R.color.white)
                    .crossFade()
                    .transform(new CircleTransform(getActivity()))
                    .into(mIvUserIcon);

            Glide.with(mIvRoomCover.getContext())
                    .load(room.getUserPicture())
                    .centerCrop()
                    .placeholder(android.R.color.white)
                    .crossFade()
                    .into(mIvRoomCover);
            mTvUserName.setText(room.getUserName());
            mTvCount.setText(room.getViewNum() + "人正在观看");
        }

        @Override
        public void onItemClick(View view, int position) {
            RoomEntity.BodyBean bean = mDataList.get(position);

            Intent intent2 = new Intent(getActivity(), LiveRoomActivity.class);
            intent2.putExtra(C.IntentKey.INTENT_KEY_ZEGO_TOKEN, 0);
            intent2.putExtra(C.IntentKey.INTENT_KEY_ZEGO_ID, Integer.valueOf(bean.getRoomId()));
            intent2.putExtra(C.IntentKey.INTENT_KEY_IS_PLAY, true);
            startActivity(intent2);
        }

    }

}
