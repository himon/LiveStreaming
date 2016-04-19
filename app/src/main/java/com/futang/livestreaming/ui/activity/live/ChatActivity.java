package com.futang.livestreaming.ui.activity.live;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.data.api.ZegoApiManager;
import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.GiftEntity;
import com.futang.livestreaming.data.entity.StopRoomEntity;
import com.futang.livestreaming.ui.base.LiveBaseActivity;
import com.futang.livestreaming.ui.module.ChatActivityModule;
import com.futang.livestreaming.ui.presenter.ChatActivityPresenter;
import com.futang.livestreaming.ui.view.IChatView;
import com.futang.livestreaming.widgets.popup.ChatPresenterPopupWindow;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zego.zegoavkit.ZegoAVApi;
import com.zego.zegoavkit.ZegoAVKitCommon;
import com.zego.zegorealtimeavkit.ZegoVideoTalk;
import com.zego.zegorealtimeavkit.ZegoVideoTalkStatus;
import com.zego.zegorealtimeavkit.callback.ZegoVideoTalkCallback;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * des: 主页面
 */
public class ChatActivity extends LiveBaseActivity implements IChatView, View.OnClickListener {

    public static final String TAG = "ChatActivity";

    /**
     * 频道id.
     */
    private int mChannelId;
    private int mUserType;
    private String mLiveName = "xxxsss";

    private ZegoVideoTalk mZegoVideoTalk;

    public SurfaceView svLocalVideo;
    public SurfaceView svRemoteVideo;
    private RelativeLayout.LayoutParams mParamsSmall;
    private RelativeLayout.LayoutParams mParamsBig;
    public RelativeLayout rlytLocalVideo;

    @Bind(R.id.rlyt_remote_video)
    RelativeLayout rlytRemoteVideo;
    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.ll_head)
    LinearLayout mLLHead;
    @Bind(R.id.iv_present)
    ImageView mIvPresenter;

    @Inject
    ChatActivityPresenter mPresenter;

    private boolean mLocalVideoIsSmall = true;

    private ChatPresenterPopupWindow mPresenterWindow;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CreateRoomEntity.BodyBean mRoom;
    private List<GiftEntity.BodyBean> mGiftList;

    /**
     * 启动入口.
     *
     * @param activity  源activity
     * @param channelId 频道ID
     */
    public static void actionStart(Activity activity, int channelId, int type) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(IntentExtra.KEY_CHANNEL_ID, channelId);
        intent.putExtra(IntentExtra.KEY_CHANNEL_TYPE, type);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initExtraData() {
        mChannelId = getIntent().getIntExtra(IntentExtra.KEY_CHANNEL_ID, 0);
        mUserType = getIntent().getIntExtra(IntentExtra.KEY_CHANNEL_TYPE, 0);
    }

    @Override
    protected void initVariables() {

        // 获取ZegoVideoTalk对象
        mZegoVideoTalk = ZegoApiManager.getInstance().getZegoVideoTalk();
        // 设置频道ID
        mZegoVideoTalk.setChannelID(mChannelId);
        // 设置回调
        mZegoVideoTalk.setZegoVideoTalkCallback(new ZegoVideoTalkCallback() {
            @Override
            public void onZegoVideoTalkStatusUpdate(ZegoVideoTalkStatus zegoVideoTalkStatus) {
                String msg = "";
                switch (zegoVideoTalkStatus) {
                    case ZegoVideoTalkStatus_Connecting:
                        msg = "正在连接...";
                        break;
                    case ZegoVideoTalkStatus_Connected:
                        msg = "已经连接上，开始发送视频";
                        ZegoAVApi api = ZegoAVApi.getAPI();
                        Map<String, Object> currentPublishInfo = api.getCurrentPublishInfo();
                        String url = currentPublishInfo.get("kZegoPublishStreamURLKey").toString();
                        mPresenter.startLive(null, mChannelId + "", "0", mLiveName, "0");
                        break;
                    case ZegoVideoTalkStatus_Disconnected:
                        msg = "已经从网络断开，停止发送视频";
                        break;
                    case ZegoVideoTalkStatus_PeerConnecting:
                        msg = "正在连接对方...";
                        break;

                    case ZegoVideoTalkStatus_PeerConnected:
                        msg = "已经连接上对方，正在接收视频";
                        break;

                    case ZegoVideoTalkStatus_PeerDisconnected:
                        msg = "对方已经断开连接，停止接收视频";
                        break;
                    case ZegoVideoTalkStatus_PeerPaused:
                        msg = "对方已经暂停声音和画面传送";
                        break;

                    default:
                        break;
                }

                tvHint.setText(msg);
            }
        });
    }


    protected void oldInitViews() {


        // SurfaceView的bug, 两个SurfaceView重叠时, 其中一个无法显示视频
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            rlytLocalVideo = (RelativeLayout) findViewById(R.id.rlyt_local_video1);
            findViewById(R.id.rlyt_local_video2).setVisibility(View.GONE);
        } else {
            rlytLocalVideo = (RelativeLayout) findViewById(R.id.rlyt_local_video2);
            findViewById(R.id.rlyt_local_video1).setVisibility(View.GONE);
        }
        svLocalVideo = new SurfaceView(this);
        rlytLocalVideo.addView(svLocalVideo);

        svRemoteVideo = new SurfaceView(this);
        rlytRemoteVideo.addView(svRemoteVideo);


        // 获取布局参数
        mParamsSmall = (RelativeLayout.LayoutParams) svLocalVideo.getLayoutParams();
        mParamsBig = (RelativeLayout.LayoutParams) svRemoteVideo.getLayoutParams();


        mZegoVideoTalk.setLocalView(svLocalVideo);
        mZegoVideoTalk.setLocalViewMode(ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);

        mZegoVideoTalk.setRemoteView(ZegoAVKitCommon.ZegoRemoteViewIndex.First, svRemoteVideo);
        mZegoVideoTalk.setRemoteViewMode(ZegoAVKitCommon.ZegoRemoteViewIndex.First, ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);


        rlytLocalVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocalVideoIsSmall) {
                    // 本地视频变大, 远程视频变小
                    rlytLocalVideo.removeView(svLocalVideo);
                    svLocalVideo.setLayoutParams(mParamsBig);

                    rlytRemoteVideo.removeView(svRemoteVideo);
                    svRemoteVideo.setLayoutParams(mParamsSmall);


                    rlytLocalVideo.addView(svRemoteVideo);
                    rlytRemoteVideo.addView(svLocalVideo);
                    mLocalVideoIsSmall = false;
                } else {
                    //  远程视频变大, 本地视频变小
                    rlytLocalVideo.removeView(svRemoteVideo);
                    svRemoteVideo.setLayoutParams(mParamsBig);

                    rlytRemoteVideo.removeView(svLocalVideo);
                    svLocalVideo.setLayoutParams(mParamsSmall);


                    rlytLocalVideo.addView(svLocalVideo);
                    rlytRemoteVideo.addView(svRemoteVideo);
                    mLocalVideoIsSmall = true;

                }
            }
        });
    }


    @Override
    protected String getPageTag() {
        return TAG;
    }

    @Override
    protected void onDestroy() {
        mPresenter.stopLive(mRoom.getId() + "", "0");
        super.onDestroy();
        // 停止通讯
        mZegoVideoTalk.stopTalking();
    }

    @OnClick({R.id.ibtn_camera, R.id.ibtn_sound, R.id.ibtn_microphone, R.id.ibtn_pause})
    public void onClickBtn(View v) {

        ImageButton ibtn = (ImageButton) v;
        if (ibtn.isSelected()) {
            ibtn.setSelected(false);
        } else {
            ibtn.setSelected(true);
        }
        switch (v.getId()) {
            case R.id.ibtn_camera:
                if (ibtn.isSelected()) {
                    //关闭前置摄像头
                    mZegoVideoTalk.setFrontCam(false);
                } else {
                    //开启前置摄像头
                    mZegoVideoTalk.setFrontCam(true);
                }
                break;
            case R.id.ibtn_sound:
                if (ibtn.isSelected()) {
                    //关闭声音
                    mZegoVideoTalk.enableSpeaker(false);
                } else {
                    //开启声音
                    mZegoVideoTalk.enableSpeaker(true);
                }
                break;
            case R.id.ibtn_microphone:
                if (ibtn.isSelected()) {
                    //关闭话筒
                    mZegoVideoTalk.enableMic(false);
                } else {

                    //开启话筒
                    mZegoVideoTalk.enableMic(true);
                }
                break;
            case R.id.ibtn_pause:
                if (ibtn.isSelected()) {
                    // 暂停通话
                    mZegoVideoTalk.pauseTalking();
                } else {
                    // 继续通话
                    mZegoVideoTalk.resumeTalking();
                }
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this).getUserComponent().plus(new ChatActivityModule(this)).inject(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(getContentViewLayout());

        // 初始化butternife
        ButterKnife.bind(this);
        initExtraData();
        initVariables();
    }

    @Override
    protected void setUpView() {
        // SurfaceView的bug, 两个SurfaceView重叠时, 其中一个无法显示视频
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            rlytLocalVideo = (RelativeLayout) findViewById(R.id.rlyt_local_video1);
            findViewById(R.id.rlyt_local_video2).setVisibility(View.GONE);
        } else {
            rlytLocalVideo = (RelativeLayout) findViewById(R.id.rlyt_local_video2);
            findViewById(R.id.rlyt_local_video1).setVisibility(View.GONE);
        }

        if (mUserType == 0) {
            svRemoteVideo = new SurfaceView(this);
            rlytRemoteVideo.addView(svRemoteVideo);
            mZegoVideoTalk.setLocalView(svRemoteVideo);
            mZegoVideoTalk.setLocalViewMode(ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);
        } else {
            svRemoteVideo = new SurfaceView(this);
            rlytRemoteVideo.addView(svRemoteVideo);
            mZegoVideoTalk.setRemoteView(ZegoAVKitCommon.ZegoRemoteViewIndex.First, svRemoteVideo);
            mZegoVideoTalk.setRemoteViewMode(ZegoAVKitCommon.ZegoRemoteViewIndex.First, ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);
        }

        initEvent();
    }

    private void initEvent() {
        mIvPresenter.setOnClickListener(this);
    }

    @Override
    protected void setUpData() {
        // 开启前置摄像头
        mZegoVideoTalk.setFrontCam(true);
        // 开始通讯
        mZegoVideoTalk.startTalking();

        mPresenter.getGift("0");
    }

    @Override
    public void setCreateRoomRes(CreateRoomEntity response) {
        mRoom = response.getBody();
    }

    @Override
    public void setStopLive(StopRoomEntity stopRoomEntity) {

    }

    @Override
    public void setGiftList(GiftEntity gift) {
        mGiftList = gift.getBody();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_present:
                presenterPopupShow();
                break;
        }
    }

    private void presenterPopupShow() {
        mPresenterWindow = new ChatPresenterPopupWindow(this, mLLHead, mGiftList);
    }
}
