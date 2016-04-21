package com.futang.livestreaming.ui.activity.live;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.TextUtilsCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.data.C;
import com.futang.livestreaming.data.api.ZegoApiManager;
import com.futang.livestreaming.data.entity.CreateRoomEntity;
import com.futang.livestreaming.data.entity.GiftEntity;
import com.futang.livestreaming.data.entity.StopRoomEntity;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.data.event.LiveRoomEvent;
import com.futang.livestreaming.data.module.ChatMessage;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.adapter.CommonAdapter;
import com.futang.livestreaming.ui.adapter.ViewHolder;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.fragment.CreateLiveRoomFragment;
import com.futang.livestreaming.ui.module.LiveRoomActivityModule;
import com.futang.livestreaming.ui.presenter.LiveRoomActivityPresenter;
import com.futang.livestreaming.ui.view.ILiveRoomView;
import com.futang.livestreaming.util.ToastUtils;
import com.futang.livestreaming.widgets.dialog.ChatGiftDialogFragment;
import com.futang.livestreaming.widgets.popup.ChatShapePopupWindow;
import com.github.lazylibrary.util.DensityUtil;
import com.github.lazylibrary.util.InputMethodUtils;
import com.zego.zegoavkit.ZegoAVApi;
import com.zego.zegoavkit.ZegoAVChatRoomCallback;
import com.zego.zegoavkit.ZegoAVKitCommon;
import com.zego.zegoavkit.ZegoAVVideoCallback;
import com.zego.zegoavkit.ZegoStreamInfo;
import com.zego.zegoavkit.ZegoUser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class LiveRoomActivity extends BaseActivity implements ILiveRoomView, View.OnClickListener, View.OnLayoutChangeListener {

    @Bind(R.id.rl_parent)
    FrameLayout mFlParent;
    @Bind(R.id.rl_controls)
    RelativeLayout mRlControls;
    @Bind(R.id.iv_present)
    ImageView mIvPresenter;
    @Bind(R.id.iv_share)
    ImageView mIvShare;
    @Bind(R.id.iv_chat)
    ImageView mIvChat;
    @Bind(R.id.iv_close_live)
    ImageView mIvCloseLive;
    @Bind(R.id.rl_menu)
    RelativeLayout mRlMenu;
    @Bind(R.id.ll_input)
    LinearLayout mLLInput;
    @Bind(R.id.et_input)
    EditText mEtInput;
    @Bind(R.id.btn_send)
    Button mBtnSendMsg;
    @Bind(R.id.lv_chat)
    ListView mListView;
    @Bind(R.id.iv_big_anim)
    ImageView mIvBigAnim;
    @Bind(R.id.rl_gift_show)
    RelativeLayout mRlGiftShow;
    @Bind(R.id.tv_gift_num)
    TextView mTvGiftNum;

    private CommonAdapter mAdapter;
    private List<ChatMessage> mList;

    //    zego
    private boolean mIsPlaying = false;
    private String mPublishTitle = null;
    private boolean mIsPublishing;
    private long mPlayingStream;
    private ZegoAVApi mZegoAVApi;
    private ZegoUser mZegoUser;
    private ZegoAVChatRoomCallback mChatRoomCallback;
    private ZegoAVVideoCallback mVideoCallback;

    @Inject
    LiveRoomActivityPresenter mPresenter;

    private ArrayList<GiftEntity.BodyBean> mGiftList;
    private UserEntity.BodyBean mUser;
    private int mZegoId;
    private CreateRoomEntity.BodyBean mRoom;

    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private FragmentManager mFragmentManager;
    private CreateLiveRoomFragment mCreateLiveRoomFragment;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    showMsg(map);
                    break;
                case 1:
                    showMsg(msg.obj.toString());
                    break;
            }
        }
    };
    /**
     * 视频rmtp
     */
    private String mPlayURL;

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this).getUserComponent().plus(new LiveRoomActivityModule(this)).inject(this);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_liveroom);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mZegoAVApi = ZegoApiManager.getInstance().getZegoAVApi();
        mZegoUser = ZegoApiManager.getInstance().getZegoUser();

        // 初始化回调
        initCallback();
        mZegoAVApi.setChatRoomCallback(mChatRoomCallback);
        mZegoAVApi.setVideoCallback(mVideoCallback);

        mUser = mPresenter.getmRepositoriesManager().getmUser().getBody();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA}, 101);
            } else {
                initViews();
            }
        } else {
            initViews();
        }

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
    }


    @Override
    protected void setUpView() {

        mFragmentManager = getSupportFragmentManager();
        mCreateLiveRoomFragment = new CreateLiveRoomFragment();
        FragmentTransaction fragmentTran = mFragmentManager.beginTransaction();
        fragmentTran.add(R.id.frame_creat_room, mCreateLiveRoomFragment);
        fragmentTran.show(mCreateLiveRoomFragment);
        fragmentTran.commitAllowingStateLoss();

        mList = new ArrayList<>();
        mAdapter = new CommonAdapter<ChatMessage>(this, mList, R.layout.layout_chat_msg_item) {
            @Override
            public void convert(ViewHolder holder, ChatMessage chatMessage) {
                SpannableStringBuilder ssb = new SpannableStringBuilder(chatMessage.getName() + " " + chatMessage.getMsg());
                ssb.setSpan(new ForegroundColorSpan(0xfe9c33), 0, chatMessage.getName().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                TextView tv = holder.getView(R.id.tv_msg);
                tv.setText(ssb);
            }
        };
        mListView.setAdapter(mAdapter);
        initEvent();
    }

    @Override
    protected void setUpData() {
        mPresenter.getGift("0");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initViews();
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!mIsPlaying) {
            stopPublish();
        }

        mZegoAVApi.leaveChatRoom();
        finish();
    }

    private void initViews() {

        Intent intent = getIntent();
        int zegoToken = intent.getIntExtra(C.IntentKey.INTENT_KEY_ZEGO_TOKEN, 0);
        mZegoId = intent.getIntExtra(C.IntentKey.INTENT_KEY_ZEGO_ID, 0);
        mIsPlaying = intent.getBooleanExtra(C.IntentKey.INTENT_KEY_IS_PLAY, false);

        int play = mIsPlaying ? 1 : 0;

        if (!mIsPlaying) {
            mPublishTitle = intent.getStringExtra(C.IntentKey.INTENT_KEY_PUBLISH_TITLE);
        }

        String s = String.format("zegoID: %d, zegoToken: %d, play: %d", mZegoId, zegoToken, play);
        addLog(s);

        if (mZegoId == 0) {
            mZegoId = Integer.valueOf(mUser.getId() + "1");
        }
        int i = mZegoAVApi.setFrontCam(true);
        mZegoAVApi.getInChatRoom(mZegoUser, zegoToken, mZegoId);

        if (mIsPlaying) {
            mZegoAVApi.setRemoteViewMode(ZegoAVKitCommon.ZegoRemoteViewIndex.First, ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);
        } else {
            mZegoAVApi.setLocalViewMode(ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);
        }
    }

    private void initEvent() {
        mIvPresenter.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvChat.setOnClickListener(this);
        mBtnSendMsg.setOnClickListener(this);
    }

    private void initCallback() {
        mChatRoomCallback = new ZegoAVChatRoomCallback() {
            @Override
            public void onGetInChatRoom(int errorCode, int zegoToken, int zegoId) {
                if (mIsPlaying) {
                    mZegoAVApi.sendBroadcastTextMsgInChatRoom("Hello from Android SDK.");
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
                                if (info.userID.equals(mZegoUser.id)) {
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
                Message message = new Message();
                message.what = 0;
                message.obj = msgInfo;
                mHandler.sendMessage(message);
            }

            @Override
            public void onReceiveBroadcastTextMsg(ZegoUser sender, int sendTime, String msg) {
                Message message = new Message();
                message.what = 1;
                message.obj = msg;
                mHandler.sendMessage(message);
            }
        };

        mVideoCallback = new ZegoAVVideoCallback() {

            @Override
            public void onPlaySucc(long streamID, int zegoToken, int zegoId) {
                mPlayingStream = streamID;
                addLog("on play begin: " + Long.toString(streamID));
            }

            @Override
            public void onPlayStop(int errorCode, long streamID, int zegoToken, int zegoId) {
                addLog("onPlayStop: " + errorCode + " " + streamID);
                if (errorCode == ZegoAVVideoCallback.NormalStop) {
                    mPlayingStream = 0;
                } else if (errorCode == ZegoAVVideoCallback.TempErr) {
                    mPlayingStream = 0;
                }
            }

            @Override
            public void onPublishSucc(int zegoToken, int zegoId) {
                addLog("onPublishStateUpdate: " + "publish started");
                mIsPublishing = true;

                Map<String, Object> publishInfo = mZegoAVApi.getCurrentPublishInfo();
                Long streamID = (Long) publishInfo.get(ZegoAVApi.kZegoPublishStreamIDKey);
                mPlayURL = (String) publishInfo.get(ZegoAVApi.kZegoPublishStreamURLKey);
//            String streamAlias = (String)publishInfo.get(ZegoAVApi.kZegoPublishStreamAliasKey);
//            addLog("onPublishStateUpdate: " + streamID.toString() + "\n" + playURL + "\n" + streamAlias);
            }

            /**
             * 直播结束
             * @param errorCode
             * @param zegoToken
             * @param zegoId
             */
            @Override
            public void onPublishStop(int errorCode, int zegoToken, int zegoId) {
                addLog("onPublishStateUpdate: " + "publish ended");
                mIsPublishing = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

            /**
             * 本地截图回调
             * @param img
             */
            @Override
            public void onTakeLocalViewSnapshot(Bitmap img) {
                addLog("onTakeLocalViewSnapshot " + img);
                showImage(img);
            }

            /**
             * 远程截图回调
             * @param img
             * @param idx
             */
            @Override
            public void onTakeRemoteViewSnapshot(Bitmap img, ZegoAVKitCommon.ZegoRemoteViewIndex idx) {
                addLog("onTakeRemoteViewSnapshot: " + img + idx);
                showImage(img);
            }

            @Override
            public void onSetPublishExtraData(int errorCode, int zegoToken, int zegoId, String key) {
                addLog("onSetPublishExtraData: " + errorCode + ":" + key);
            }
        };
    }

    private void showMsg(Map<String, Object> msg) {
        ChatMessage message = new ChatMessage();
        message.setMsg(msg.get(mZegoAVApi.SYNC_COUNT_0).toString());
        message.setName(msg.get(mZegoAVApi.SYNC_COUNT_1).toString());
        mList.add(message);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mList.size() - 1);
    }

    private void showMsg(String msg) {
        if (msg.startsWith("@")) {
            ChatMessage message = new ChatMessage();
            int index = msg.indexOf(" ");
            message.setName(msg.substring(1, index));
            message.setMsg(msg.substring(index, msg.length()));
            mList.add(message);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mList.size() - 1);
        }
    }

    /**
     * 显示截图
     *
     * @param img
     */
    public void showImage(Bitmap img) {
        final Bitmap bm = img;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    /**
     * 开始播放
     *
     * @param streamID
     */
    public void startPlay(long streamID) {
        addLog("startPlay");
        final long sID = streamID;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View rv = remoteView();
                if (rv != null) {
                    mZegoAVApi.setRemoteView(ZegoAVKitCommon.ZegoRemoteViewIndex.First, rv);
                    mZegoAVApi.startPlayInChatRoom(ZegoAVKitCommon.ZegoRemoteViewIndex.First, sID);
                }
            }
        });
    }


    public View remoteView() {
        View rv = findViewById(R.id.svVideoView);
        return rv;
    }

    /**
     * 开始直播
     */
    public void startPublish() {
        addLog("startPublish");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View lv = remoteView();
                if (lv != null) {
                    mZegoAVApi.setLocalView(lv);
                    mZegoAVApi.startPreview();
                    if (mIsPlaying) {
                        mZegoAVApi.enableMic(false);
                    } else {
                        mZegoAVApi.enableMic(true);
                    }
                    mZegoAVApi.enableTorch(false);
                    mZegoAVApi.startPublishInChatRoom(mPublishTitle);
                }
            }
        });
    }


    /**
     * 结束直播
     */
    private void stopPublish() {
        mPresenter.stopLive(mRoom.getId() + "", "0");
        mZegoAVApi.stopPublishInChatRoom();
        mZegoAVApi.stopPreview();
    }


    private void addLog(String msg) {
        Log.i("LiveRoomActivity", msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_present:
                if (mGiftList == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(C.IntentKey.MESSAGE_INTENT_KEY, mGiftList);
                ChatGiftDialogFragment chatGiftDialogFragment = new ChatGiftDialogFragment();
                chatGiftDialogFragment.setArguments(bundle);
                chatGiftDialogFragment.show(getSupportFragmentManager(), "ChatGiftDialogFragment");
                break;
            case R.id.iv_share:
                ChatShapePopupWindow noRewardMenuWindow = new ChatShapePopupWindow(this);
                noRewardMenuWindow.showAtLocation(mRlControls, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_chat:
                mRlMenu.setVisibility(View.GONE);
                mLLInput.setVisibility(View.VISIBLE);
                InputMethodUtils.openSoftKeyboard(this, mEtInput);
                break;
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.iv_close_live:
                if (mIsPublishing) {
                    stopPublish();
                }
                break;
        }
    }

    private void sendMsg() {
        String input = mEtInput.getText().toString().trim();
        if (!TextUtils.isEmpty(input)) {
            ChatMessage msg = new ChatMessage();
            msg.setMsg(input);
            msg.setName(mUser.getUserName());
            mList.add(msg);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mList.size() - 1);
            mEtInput.setText("");
//            Map<String, Object> map = new HashMap<>();
//            map.put(mZegoAVApi.SYNC_COUNT_0, msg.getName());
//            map.put(mZegoAVApi.SYNC_COUNT_1, msg.getMsg());
//            mZegoAVApi.sendBroadcastCustomMsgInChatRoom(map);
            mZegoAVApi.sendBroadcastTextMsgInChatRoom("@" + msg.getName() + " " + msg.getMsg());
        }
    }

    @Override
    public void setCreateRoomRes(CreateRoomEntity response) {
        mRoom = response.getBody();
    }

    @Override
    public void setStopLive(StopRoomEntity stopRoomEntity) {
        ToastUtils.showToast(this, "退出了直播!");
    }

    @Override
    public void setGiftList(GiftEntity gift) {
        mGiftList = gift.getBody();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

        //System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
        //System.out.println(left + " " + top +" " + right + " " + bottom);


        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            mRlMenu.setVisibility(View.VISIBLE);
            mLLInput.setVisibility(View.GONE);
        }
    }

    public void onEvent(LiveRoomEvent event) {
        if ("close".equals(event.getType())) {
            finish();
        } else if ("start_publish".equals(event.getType())) {
            mPublishTitle = event.getMsg();
            if (mCreateLiveRoomFragment != null) {
                FragmentTransaction fragmentTran = mFragmentManager
                        .beginTransaction();
                fragmentTran.remove(mCreateLiveRoomFragment);
                fragmentTran.commitAllowingStateLoss();
                mCreateLiveRoomFragment = null;
            }
            mRlControls.setVisibility(View.VISIBLE);
            mPresenter.startLive(null, mZegoId + "", "0", mPublishTitle, "0", mPlayURL, "");
        } else if ("send_gift".equals(event.getType())) {
            Point screenSize = DensityUtil.getScreenSize(this);
            int px = DensityUtil.dip2px(this, 205);
            //ObjectAnimator.ofFloat(mIvBigAnim, "translationX", screenSize.x + px, 0).setDuration(5000).start();

            if (gift == 1) {
                ObjectAnimator.ofFloat(mRlGiftShow, "translationX", 0, px).setDuration(500).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        numGiftAnim();
                    }
                }, 500);
            } else {
                numGiftAnim();
            }
        }
    }

    int gift = 1;

    private void numGiftAnim() {
        mTvGiftNum.setText(gift + "");
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mTvGiftNum, "scaleX",
                1.0f, 2.5f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mTvGiftNum, "scaleY",
                1.0f, 2.5f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();
        gift++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
