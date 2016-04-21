package com.futang.livestreaming.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futang.livestreaming.R;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.entity.GiftEntity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.base.BaseFragment;
import com.futang.livestreaming.widgets.CircleTransform;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatGiftFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private ArrayList<GiftEntity.BodyBean> mList;

    public ChatGiftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_gift, container, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    private void initData() {
        Bundle bundle = getArguments();
        mList = bundle.getParcelableArrayList(C.IntentKey.MESSAGE_INTENT_KEY);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        HomeAdapter mAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void setupActivityComponent() {

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.adapter_chat_gift_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            GiftEntity.BodyBean bodyBean = mList.get(position);
            Glide.with(getActivity())
                    .load(bodyBean.getPictureUrl())
                    .centerCrop()
                    .placeholder(android.R.color.white)
                    .crossFade()
                    .into(holder.iv);
            holder.mTvName.setText(bodyBean.getGiftName());
            holder.mTvMoeny.setText(bodyBean.getGiftMoney() + "");
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv;
            TextView mTvName;
            TextView mTvMoeny;

            public MyViewHolder(View view) {
                super(view);
                iv = (ImageView) view.findViewById(R.id.imageview);
                mTvName = (TextView) view.findViewById(R.id.tv_name);
                mTvMoeny = (TextView) view.findViewById(R.id.tv_money);
            }
        }
    }
}
