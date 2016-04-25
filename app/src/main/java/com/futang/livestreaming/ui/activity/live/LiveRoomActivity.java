package com.futang.livestreaming.ui.activity.live;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.futang.livestreaming.ui.adapter.CommonAdapter;
import com.futang.livestreaming.ui.adapter.ViewHolder;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.fragment.CreateLiveRoomFragment;
import com.futang.livestreaming.ui.module.LiveRoomActivityModule;
import com.futang.livestreaming.ui.presenter.LiveRoomActivityPresenter;
import com.futang.livestreaming.ui.view.ILiveRoomView;
import com.futang.livestreaming.util.ToastUtils;
import com.futang.livestreaming.widgets.CircleTransform;
import com.futang.livestreaming.widgets.dialog.ChatGiftDialogFragment;
import com.futang.livestreaming.widgets.dialog.GiftCountDialogFragment;
import com.futang.livestreaming.widgets.popup.ChatShapePopupWindow;
import com.github.lazylibrary.util.DensityUtil;
import com.github.lazylibrary.util.InputMethodUtils;
import com.zego.zegoavkit.ZegoAVApi;
import com.zego.zegoavkit.ZegoAVChatRoomCallback;
import com.zego.zegoavkit.ZegoAVKitCommon;
import com.zego.zegoavkit.ZegoAVVideoCallback;
import com.zego.zegoavkit.ZegoStreamInfo;
import com.zego.zegoavkit.ZegoUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.yifeiyuan.library.PeriscopeLayout;


public class LiveRoomActivity extends BaseActivity implements GiftCountDialogFragment.GiftCountDialogListener, ILiveRoomView, View.OnClickListener, View.OnLayoutChangeListener {

    /**
     * 父view
     */
    @Bind(R.id.rl_parent)
    FrameLayout mFlParent;
    /**
     * 直播控件
     */
    @Bind(R.id.rl_controls)
    RelativeLayout mRlControls;
    /**
     * 送礼物按钮
     */
    @Bind(R.id.iv_present)
    ImageView mIvPresenter;
    /**
     * 分享按钮
     */
    @Bind(R.id.iv_share)
    ImageView mIvShare;
    /**
     * 聊天按钮
     */
    @Bind(R.id.iv_chat)
    ImageView mIvChat;
    /**
     * 关注按钮
     */
    @Bind(R.id.iv_like)
    ImageView mIvLike;
    /**
     * 关闭直播按钮
     */
    @Bind(R.id.iv_close_live)
    ImageView mIvCloseLive;
    /**
     * 底部菜单
     */
    @Bind(R.id.rl_menu)
    RelativeLayout mRlMenu;
    /**
     * 聊天菜单
     */
    @Bind(R.id.ll_input)
    LinearLayout mLLInput;
    /**
     * 文字输入框
     */
    @Bind(R.id.et_input)
    EditText mEtInput;
    /**
     * 文字发送按钮
     */
    @Bind(R.id.btn_send)
    Button mBtnSendMsg;
    /**
     * 聊天内容显示框
     */
    @Bind(R.id.lv_chat)
    ListView mListView;
    /**
     * 大动画
     */
    @Bind(R.id.iv_big_anim)
    ImageView mIvBigAnim;
    /**
     * 连续动画
     */
    @Bind(R.id.rl_gift_show)
    RelativeLayout mRlGiftShow;
    /**
     * 连续动画数字
     */
    @Bind(R.id.tv_gift_num)
    TextView mTvGiftNum;
    /**
     * 连续动画发送者头像
     */
    @Bind(R.id.iv_send_user_icon)
    ImageView mIvSendUserIcon;
    /**
     * 连续动画发送者名字
     */
    @Bind(R.id.tv_send_user)
    TextView mTvSendUserName;
    /**
     * 连续动画礼物名字
     */
    @Bind(R.id.tv_gift_name)
    TextView mTvGiftName;
    /**
     * 连续动画礼物图片
     */
    @Bind(R.id.iv_gift_icon)
    ImageView mIvGiftIcon;
    /**
     * 关注效果动画
     */
    @Bind(R.id.periscope)
    PeriscopeLayout mPeriscopeLayout;


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
                    //Map<String, Object> map = (Map<String, Object>) msg.obj;
                    //showGiftAnim(map);
                    break;
                case 1:
                    showMsg(msg.obj.toString());
                    break;
                case 2:
                    showGiftAnim(msg.obj.toString());
                    break;
                case 3:
                    showLike();
                    break;
            }
        }
    };


    /**
     * 视频rmtp
     */
    private String mPlayURL;
    private GestureDetector mGestureDetector;

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
        mGestureDetector = new GestureDetector(this, new LearnGestureListener());
    }


    @Override
    protected void setUpView() {

        mList = new ArrayList<>();
        mAdapter = new CommonAdapter<ChatMessage>(this, mList, R.layout.layout_chat_msg_item) {
            @Override
            public void convert(ViewHolder holder, ChatMessage chatMessage) {
                SpannableStringBuilder ssb = new SpannableStringBuilder(chatMessage.getName() + " " + chatMessage.getMsg());
                ssb.setSpan(new ForegroundColorSpan(0x99fe9c33), 0, chatMessage.getName().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        mFlParent.addOnLayoutChangeListener(this);
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
            mRlControls.setVisibility(View.VISIBLE);
        } else {
            mZegoAVApi.setLocalViewMode(ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);
            mFragmentManager = getSupportFragmentManager();
            mCreateLiveRoomFragment = new CreateLiveRoomFragment();
            Bundle bundle = new Bundle();
            bundle.putString(C.IntentKey.MESSAGE_INTENT_KEY, mUser.getPicture());
            mCreateLiveRoomFragment.setArguments(bundle);
            FragmentTransaction fragmentTran = mFragmentManager.beginTransaction();
            fragmentTran.add(R.id.frame_creat_room, mCreateLiveRoomFragment);
            fragmentTran.show(mCreateLiveRoomFragment);
            fragmentTran.commitAllowingStateLoss();
        }
    }

    private void initEvent() {
        mIvPresenter.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvChat.setOnClickListener(this);
        mBtnSendMsg.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        mIvCloseLive.setOnClickListener(this);
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
//                Message message = new Message();
//                message.what = 0;
//                message.obj = msgInfo;
//                mHandler.sendMessage(message);
            }

            @Override
            public void onReceiveBroadcastTextMsg(ZegoUser sender, int sendTime, String msg) {
                int index = 1;
                if (msg.startsWith("@")) {
                    index = 1;
                } else if (msg.startsWith("*")) {
                    index = 2;
                } else if (msg.startsWith("#")) {
                    index = 3;
                }

                Message message = new Message();
                message.what = index;
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

                if (mCreateLiveRoomFragment == null) {
                    mPresenter.startLive(null, mZegoId + "", "0", mPublishTitle, "0", mPlayURL, "");
                }
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

    /**
     * 显示礼物动画
     *
     * @param msg
     */
    private void showGiftAnim(String msg) {

        String[] split = msg.split(" ");

        int id = Integer.valueOf(split[0].substring(1, split[0].length()));
        int count = Integer.valueOf(split[1]);

        GiftEntity.BodyBean gift = null;
        for (int i = 0; i < mGiftList.size(); i++) {
            GiftEntity.BodyBean bodyBean = mGiftList.get(i);
            if (id == bodyBean.getId()) {
                gift = bodyBean;
                break;
            }
        }
        if (gift != null) {
            giftAnim(count, gift, split[2]);
        }
    }

    /**
     * 显示关注动画
     */
    private void showLike() {
        mPeriscopeLayout.addHeart();
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
                mCreateLiveRoomFragment.setBackground();

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
        if (mRoom != null) {
            mPresenter.stopLive(mRoom.getId() + "", "0");
        }
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
            case R.id.iv_like:
                mPeriscopeLayout.addHeart();
                mZegoAVApi.sendBroadcastTextMsgInChatRoom("##");
                break;
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.iv_close_live:
                onBackPressed();
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
        if ("close".equals(event.getType())) {//关闭页面
            onBackPressed();
        } else if ("start_publish".equals(event.getType())) {//开启直播
            mPublishTitle = event.getMsg();
            if (mCreateLiveRoomFragment != null) {
                FragmentTransaction fragmentTran = mFragmentManager
                        .beginTransaction();
                fragmentTran.remove(mCreateLiveRoomFragment);
                fragmentTran.commitAllowingStateLoss();
                mCreateLiveRoomFragment = null;
            }
            mRlControls.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mPlayURL)) {
                mPresenter.startLive(null, mZegoId + "", "0", mPublishTitle, "0", mPlayURL, "");
            }
            hideKeyboard();
        } else if ("send_gift".equals(event.getType())) {//发送礼物
            sendGift(event.getIndex(), event.getGift());
            hideKeyboard();
        }
    }

    private void sendGift(int count, GiftEntity.BodyBean gift) {
        mZegoAVApi.sendBroadcastTextMsgInChatRoom("*" + gift.getId() + " " + count + " " + mUser.getPicture());
        if (count == 0) {
            Glide.with(this)
                    .load(gift.getPictureUrl())
                    .centerCrop()
                    .crossFade()
                    .into(mIvBigAnim);

            Point screenSize = DensityUtil.getScreenSize(this);
            int px = DensityUtil.dip2px(this, 300);
            ObjectAnimator.ofFloat(mIvBigAnim, "translationX", screenSize.x + px, 0).setDuration(5000).start();
        } else {
            giftAnim(count, gift, mUser.getPicture());
        }
    }

    /**
     * 礼物动画展示
     *
     * @param count
     * @param gift,
     */
    private void giftAnim(int count, GiftEntity.BodyBean gift, String url) {

        mTvGiftName.setText("送了 " + gift.getGiftName());
        mTvSendUserName.setText(mUser.getUserName());
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(android.R.color.white)
                .crossFade()
                .transform(new CircleTransform(this))
                .into(mIvSendUserIcon);

        Glide.with(this)
                .load(gift.getPictureUrl())
                .centerCrop()
                .placeholder(android.R.color.white)
                .crossFade()
                .into(mIvGiftIcon);

        int px = DensityUtil.dip2px(this, 305);
        ObjectAnimator.ofFloat(mRlGiftShow, "translationX", 0, px).setDuration(500).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startNumGiftAnim(1, count);
            }
        }, 500);
    }


    private void startNumGiftAnim(int index, int count) {
        numGiftAnim(index, count);
    }

    private void numGiftAnim(int index, int count) {
        mTvGiftNum.setText("x" + index);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mTvGiftNum, "scaleX",
                1.0f, 2.5f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mTvGiftNum, "scaleY",
                1.0f, 2.5f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (index < count) {
                    startNumGiftAnim(index + 1, count);
                } else {
                    int px = DensityUtil.dip2px(LiveRoomActivity.this, 305);
                    ObjectAnimator.ofFloat(mRlGiftShow, "translationX", px, 0).setDuration(500).start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animSet.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        else
            return false;
    }

    @Override
    public void confirm(int count) {

    }

    private class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            Log.d("onSingleTapUp", ev.toString());
            return true;
        }

        @Override
        public void onShowPress(MotionEvent ev) {
            Log.d("onShowPress", ev.toString());
        }

        @Override
        public void onLongPress(MotionEvent ev) {
            Log.d("onLongPress", ev.toString());
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("onScroll", e1.toString());
            return true;
        }

        @Override
        public boolean onDown(MotionEvent ev) {
            Log.d("onDownd", ev.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getRawY() - e2.getRawY()) < 90) {
                if (e1.getRawX() > e2.getRawX()) {
                    mRlControls.setVisibility(View.VISIBLE);
                } else {
                    mRlControls.setVisibility(View.INVISIBLE);
                }
            }
            return true;
        }
    }
}
