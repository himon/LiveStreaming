package com.futang.livestreaming.ui.activity.live;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.api.ZegoApi;
import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.module.LivePublishActivityModule;
import com.futang.livestreaming.ui.presenter.LivePublishActivityPresenter;
import com.futang.livestreaming.ui.view.ILivePublishView;
import com.zego.zegoavkit.ZegoAVApi;
import com.zego.zegoavkit.ZegoAVChatRoomCallback;
import com.zego.zegoavkit.ZegoAVKitCommon;
import com.zego.zegoavkit.ZegoAVVideoCallback;
import com.zego.zegoavkit.ZegoStreamInfo;
import com.zego.zegoavkit.ZegoUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.plugins.RxAndroidPlugins;

public class LivePublishActivity extends BaseActivity implements ILivePublishView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.sv_video_view)
    SurfaceView mSvVideoView;
    @Bind(R.id.tv_user_count)
    TextView mTvUserCount;
    @Bind(R.id.cb_flash)
    CheckBox mCbFlash;
    @Bind(R.id.cb_frontCam)
    CheckBox mCbFrontCam;
    @Bind(R.id.btn_close)
    ImageButton mIbClose;
    @Bind(R.id.cb_mic)
    CheckBox mCbMic;

    private String _publishTitle = null;
    private boolean _publishing;
    private boolean _play = false;
    private long _playingStream;
    private ZegoUser _user = null;
    private int inChatRoom;

    @Inject
    LivePublishActivityPresenter mPresenter;

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this).getUserComponent().plus(new LivePublishActivityModule(this)).inject(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_live_publish);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
        setupAVKit();
        initUser();
        initEvent();
    }

    @Override
    protected void setUpData() {
        Intent intent = getIntent();
        _play = intent.getBooleanExtra(C.IntentKey.INTENT_KEY_IS_PLAY, false);
        int play = _play ? 1 : 0;
        if (!_play) {
            _publishTitle = intent.getStringExtra(C.IntentKey.INTENT_KEY_PUBLISH_TITLE);
        }
        //进入聊天室
        inChatRoom = ZegoApi.getAPI().getInChatRoom(_user, 0, 1);
    }

    private void initEvent() {
        mIbClose.setOnClickListener(this);
        mCbFlash.setOnCheckedChangeListener(this);
        mCbFrontCam.setOnCheckedChangeListener(this);
        mCbMic.setOnCheckedChangeListener(this);
    }

    private void setupAVKit() {
        ZegoApi.getAPI().setChatRoomCallback(_chatCB);
        ZegoApi.getAPI().setVideoCallback(_videoCB);
    }

    private void initUser() {
        _user = new ZegoUser();
        UserEntity.BodyBean user = mPresenter.getmRepositoriesManager().getmUser().getBody();
        _user.id = user.getId();
        _user.name = user.getUserName();
    }

    /**
     * 开启直播
     */
    public void startPublish() {
        addLog("startPublish");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ZegoApi.getAPI().setLocalView(mSvVideoView);
                ZegoApi.getAPI().startPreview();
                ZegoApi.getAPI().setFrontCam(false);
                ZegoApi.getAPI().enableMic(mCbMic.isChecked());
                ZegoApi.getAPI().enableTorch(mCbFlash.isChecked());
                ZegoApi.getAPI().startPublishInChatRoom(_publishTitle);
            }
        });
    }

    public void startPlay(long streamID) {
        addLog("startPlay");
        final long sID = streamID;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View rv = remoteView();
                if (rv != null) {
                    ZegoApi.getAPI().setRemoteView(ZegoAVKitCommon.ZegoRemoteViewIndex.First, rv);
                    ZegoApi.getAPI().startPlayInChatRoom(ZegoAVKitCommon.ZegoRemoteViewIndex.First, sID);
                }
            }
        });
    }

    /**
     * 退出直播室
     */
    private void stopPublish() {
        ZegoApi.getAPI().stopPublishInChatRoom();
        ZegoApi.getAPI().stopPreview();
        finish();
    }

    public View remoteView() {
        View rv = findViewById(R.id.sv_video_view);
        return rv;
    }

    final ZegoAVChatRoomCallback _chatCB = new ZegoAVChatRoomCallback() {
        @Override
        public void onGetInChatRoom(int errorCode, int zegoToken, int zegoId) {
            if (_play) {
                ZegoApi.getAPI().sendBroadcastTextMsgInChatRoom("Hello from Android SDK.");
            } else {
                startPublish();
            }
        }

        @Override
        public void onPlayListUpdate(ZegoAVKitCommon.ZegoPlayListUpdateFlag flag, ZegoStreamInfo[] streamInfoList) {
            switch (flag) {
                case Error:
                    break;
                case Add:
                case UpdateAll:
                    if (streamInfoList != null && streamInfoList.length > 0) {
                        for (ZegoStreamInfo info : streamInfoList) {
                            if (info.userID.equals(_user.id)) {
                                continue;
                            }
                            addLog(String.format("Trying to play stream: %d, title: ", info.streamID) + info.streamTitle);
                            startPlay(info.streamID);
                        }
                    }
                    break;
                case Remove:
                    break;
            }
        }

        @Override
        public void onChatRoomUserUpdate(ZegoUser[] newUserList, ZegoUser[] leftUserList) {
        }

        @Override
        public void onChatRoomUserUpdateAll(ZegoUser[] allUserList) {
        }

        @Override
        public void onReceiveBroadcastCustomMsg(ZegoUser sender, int sendTime, Map<String, Object> msgInfo) {
        }

        @Override
        public void onReceiveBroadcastTextMsg(ZegoUser sender, int sendTime, String msg) {
            addLog("onReceiveBroadcastCustomMsg" + msg);
        }
    };

    private void addLog(String msg) {
        Log.i("LiveRoomActivity", msg);
    }


    final ZegoAVVideoCallback _videoCB = new ZegoAVVideoCallback() {

        @Override
        public void onPlaySucc(long streamID, int zegoToken, int zegoId) {
            _playingStream = streamID;
            addLog("on play begin: " + Long.toString(streamID));
        }

        @Override
        public void onPlayStop(int errorCode, long streamID, int zegoToken, int zegoId) {
            addLog("onPlayStop: " + errorCode + " " + streamID);
            if (errorCode == ZegoAVVideoCallback.NormalStop) {
                _playingStream = 0;
            } else if (errorCode == ZegoAVVideoCallback.TempErr) {
                _playingStream = 0;
            }
        }

        @Override
        public void onPublishSucc(int zegoToken, int zegoId) {
            addLog("onPublishStateUpdate: " + "publish started");
            _publishing = true;

            Map<String, Object> publishInfo = ZegoApi.getAPI().getCurrentPublishInfo();
            Long streamID = (Long) publishInfo.get(ZegoAVApi.kZegoPublishStreamIDKey);
            String playURL = (String) publishInfo.get(ZegoAVApi.kZegoPublishStreamURLKey);
            addLog("onPublishStateUpdate: " + streamID.toString() + "\n" + playURL);

            ZegoAVKitCommon.ZegoVideoViewMode mode = ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill;
            ZegoApi.getAPI().setLocalViewMode(mode);

            Random random = new Random(10000);
            mPresenter.startLive(null, random.nextInt() + "", "0", "xxxx", "0");
        }

        @Override
        public void onPublishStop(int errorCode, int zegoToken, int zegoId) {
            addLog("onPublishStateUpdate: " + "publish ended");
            _publishing = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //localViewFrame().setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        public void onPlayerCountUpdate(int count) {
            addLog("onPlayerCountUpdate: " + Integer.toString(count));
            final int c = count;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvUserCount.setText(String.format("%d人", c));
                }
            });
        }

        @Override
        public void onTakeLocalViewSnapshot(Bitmap img) {
            addLog("onTakeLocalViewSnapshot " + img);
        }

        @Override
        public void onTakeRemoteViewSnapshot(Bitmap img, ZegoAVKitCommon.ZegoRemoteViewIndex idx) {
            addLog("onTakeRemoteViewSnapshot: " + img + idx);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                if (_publishing) {
                    stopPublish();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_flash:
                ZegoApi.getAPI().enableTorch(isChecked);
                break;
            case R.id.cb_frontCam:
                ZegoApi.getAPI().setFrontCam(isChecked);
                break;
            case R.id.cb_mic:
                ZegoApi.getAPI().enableMic(isChecked);
                break;
        }
    }

    @Override
    public void setCreateRoomRes(CreateRoomEntity response) {

    }
}
