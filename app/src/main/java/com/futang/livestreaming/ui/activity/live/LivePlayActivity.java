package com.futang.livestreaming.ui.activity.live;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.futang.livestreaming.R;
import com.futang.livestreaming.data.C;
import com.zego.zegoavkit.ZegoAVApi;
import com.zego.zegoavkit.ZegoAVChatRoomCallback;
import com.zego.zegoavkit.ZegoAVKitCommon;
import com.zego.zegoavkit.ZegoAVVideoCallback;
import com.zego.zegoavkit.ZegoStreamInfo;
import com.zego.zegoavkit.ZegoUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LivePlayActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.et_message)
    EditText mEtMessage;
    @Bind(R.id.btn_send_message)
    Button mBtnSend;
    @Bind(R.id.btnClose)
    ImageButton mIbClose;
    @Bind(R.id.btnRemoteViewShot)
    ImageButton mIbTakeViewShot;
    @Bind(R.id.ivSnapshot)
    ImageView mIvSnapShot;

    private boolean _play = false;
    private boolean _publishing;
    private long _playingStream;
    private ZegoUser _user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_play);
        ButterKnife.bind(this);
        initVIew();
        initData();
    }

    private void initVIew() {
        setupAVKit();
        initUser();
        initEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        int zegoToken = intent.getIntExtra(C.IntentKey.INTENT_KEY_ZEGO_TOKEN, 0);
        int zegoId = intent.getIntExtra(C.IntentKey.INTENT_KEY_ZEGO_ID, 0);
        _play = intent.getBooleanExtra(C.IntentKey.INTENT_KEY_IS_PLAY, false);

        int play = _play ? 1 : 0;

        //进入聊天室
        //ZegoApi.getAPI().getInChatRoom(_user, zegoToken, zegoId);
    }

    private void initEvent() {
        mBtnSend.setOnClickListener(this);
        mIbClose.setOnClickListener(this);
        mIbTakeViewShot.setOnClickListener(this);
        mIvSnapShot.setOnClickListener(this);
    }

    private void setupAVKit() {
//        ZegoApi.getAPI().setChatRoomCallback(_chatCB);
//        ZegoApi.getAPI().setVideoCallback(_videoCB);
    }

    private void initUser() {
        _user = new ZegoUser();
        Random r = new Random();
        _user.id = Integer.toString(r.nextInt());
        _user.name = _user.id;
    }

    public void startPlay(long streamID) {
        addLog("startPlay");
        final long sID = streamID;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View rv = remoteView();
                if (rv != null) {
//                    ZegoApi.getAPI().setRemoteView(ZegoAVKitCommon.ZegoRemoteViewIndex.First, rv);
//                    ZegoApi.getAPI().startPlayInChatRoom(ZegoAVKitCommon.ZegoRemoteViewIndex.First, sID);
                }
            }
        });
    }

    /**
     * 退出直播室
     */
    private void stopPublish() {
//        ZegoApi.getAPI().stopPublishInChatRoom();
//        ZegoApi.getAPI().stopPreview();
        finish();
    }

    public View remoteView() {
        View rv = findViewById(R.id.svVideoView);
        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLog("remote view clicked!");

                Map<String, Object> counter = new HashMap<String, Object>();
                counter.put(ZegoAVApi.SYNC_COUNT_0, 1);
                byte[] d = new byte[8];
                d[0] = 0;
                d[1] = 0;
                d[2] = 0;
                d[3] = 1;

                d[4] = 0;
                d[5] = 0;
                d[6] = 0;
                d[7] = 1;

                counter.put(ZegoAVApi.CUSTOM_DATA, d);
//                ZegoApi.getAPI().sendBroadcastCustomMsgInChatRoom(counter);
            }
        });
        return rv;
    }

    private void addLog(String msg) {
        Log.i("LiveRoomActivity", msg);
    }

    public void showImage(Bitmap img) {
        final Bitmap bm = img;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView iv = (ImageView) findViewById(R.id.ivSnapshot);
                if (iv != null) {
                    iv.setImageBitmap(bm);
                    iv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    final ZegoAVChatRoomCallback _chatCB = new ZegoAVChatRoomCallback() {
        @Override
        public void onGetInChatRoom(int errorCode, int zegoToken, int zegoId) {
            if (_play) {//观看
//                ZegoApi.getAPI().sendBroadcastTextMsgInChatRoom("Hello from Android SDK.");
            } else {//直播

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
            Log.i("LiveRoomActivity", "onChatRoomUserUpdate");
        }

        @Override
        public void onChatRoomUserUpdateAll(ZegoUser[] allUserList) {
            Log.i("LiveRoomActivity", "onChatRoomUserUpdate");
        }

        @Override
        public void onReceiveBroadcastCustomMsg(ZegoUser sender, int sendTime, Map<String, Object> msgInfo) {
            addLog("onReceiveBroadcastCustomMsg" + msgInfo);
        }

        @Override
        public void onReceiveBroadcastTextMsg(ZegoUser sender, int sendTime, String msg) {
            addLog("onReceiveBroadcastCustomMsg" + msg);
        }
    };


    final ZegoAVVideoCallback _videoCB = new ZegoAVVideoCallback() {

        @Override
        public void onPlaySucc(long streamID, int zegoToken, int zegoId) {
            _playingStream = streamID;
            addLog("on play begin: " + Long.toString(streamID));
            ZegoAVKitCommon.ZegoVideoViewMode mode = ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill;
//            ZegoApi.getAPI().setLocalViewMode(mode);
            //ZegoApi.getAPI().enableMic(false);
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

//            Map<String, Object> publishInfo = ZegoApi.getAPI().getCurrentPublishInfo();
//            Long streamID = (Long) publishInfo.get(ZegoAVApi.kZegoPublishStreamIDKey);
//            String playURL = (String) publishInfo.get(ZegoAVApi.kZegoPublishStreamURLKey);
//            addLog("onPublishStateUpdate: " + streamID.toString() + "\n" + playURL);
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
                    TextView v = (TextView) findViewById(R.id.TxtUserCount);
                    v.setText(String.format("%d", c));
                }
            });
        }

        @Override
        public void onTakeLocalViewSnapshot(Bitmap img) {
            addLog("onTakeLocalViewSnapshot " + img);
            showImage(img);
        }

        @Override
        public void onTakeRemoteViewSnapshot(Bitmap img, ZegoAVKitCommon.ZegoRemoteViewIndex idx) {
            addLog("onTakeRemoteViewSnapshot: " + img + idx);
            showImage(img);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_message:
                sendMessage();
                break;
            case R.id.btnClose:
                if (_publishing) {
                    stopPublish();
                }
                break;
            case R.id.btnRemoteViewShot://截图
//                ZegoApi.getAPI().takeRemoteViewSnapshot(ZegoAVKitCommon.ZegoRemoteViewIndex.First);
                break;
            case R.id.ivSnapshot:
                v.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!_play) {
            stopPublish();
        }

//        ZegoApi.getAPI().leaveChatRoom();
        finish();
    }

    private void sendMessage() {
        String message = mEtMessage.getText().toString();
        long code = ZegoAVApi.getAPI().sendBroadcastTextMsgInChatRoom(message);
        if(code != 0){
            Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
        }
    }
}
