package com.futang.livestreaming.ui.fragment;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.futang.livestreaming.R;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.event.LiveRoomEvent;
import com.futang.livestreaming.util.BlurBitmapUtil;
import com.futang.livestreaming.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateLiveRoomFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.et_title)
    EditText mEtTitle;
    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.btn_live)
    Button mBtnStart;
    @Bind(R.id.rl_parent)
    RelativeLayout mRlParent;

    public CreateLiveRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_live_room, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        Bundle bundle = getArguments();
        String url = bundle.getString(C.IntentKey.MESSAGE_INTENT_KEY);
        int width = 480;
        int height = 800;
        Glide.with(getActivity())
                .load(url)
                .asBitmap()
                .fitCenter()
                .placeholder(R.color.colorPrimary)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        Bitmap bitmap = BlurBitmapUtil.fastblur(getActivity(), resource, 10);
                        BitmapDrawable drawable = new BitmapDrawable(bitmap);
                        mRlParent.setBackgroundDrawable(drawable);
                    }
                });
    }

    public void setBackground() {
        mRlParent.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
    }

    private void initView() {
        mBtnStart.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_live:
                if (TextUtils.isEmpty(mEtTitle.getText().toString().trim())) {
                    ToastUtils.showToast(getActivity(), "请输入房间名");
                    return;
                }
                LiveRoomEvent liveRoomEvent = new LiveRoomEvent("start_publish");
                liveRoomEvent.setMsg(mEtTitle.getText().toString().trim());
                EventBus.getDefault().post(liveRoomEvent);
                break;
            case R.id.iv_close:
                EventBus.getDefault().post(new LiveRoomEvent("close"));
                break;
        }
    }
}
