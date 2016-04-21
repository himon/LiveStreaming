package com.futang.livestreaming.widgets.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.futang.livestreaming.R;
import com.google.common.eventbus.EventBus;


/**
 * Created by lc on 16/4/19.
 */
public class ChatShapePopupWindow extends PopupWindow implements View.OnClickListener {

    private Activity mContext;

    private View mMenuView = null;
    private LinearLayout mLLWechat;
    private LinearLayout mLLWechatmoments;
    private LinearLayout mLLQzone;
    private LinearLayout mLLQQ;
    private Button mBtnCancel;

    public ChatShapePopupWindow(Activity context) {
        super(context);
        this.mContext = context;
        initView();
        initData();
    }

    private void initData() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xB2B2B2);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                clear();
            }
        });
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.window_chat_share, null);
        mLLWechat = (LinearLayout) mMenuView.findViewById(R.id.ll_wechat);
        mLLWechatmoments = (LinearLayout) mMenuView
                .findViewById(R.id.ll_wechatmoments);
        mLLQzone = (LinearLayout) mMenuView.findViewById(R.id.ll_qzone);
        mLLQQ = (LinearLayout) mMenuView.findViewById(R.id.ll_qq);
        mBtnCancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        mLLQzone.setOnClickListener(this);
        mLLQQ.setOnClickListener(this);
        mLLWechat.setOnClickListener(this);
        mLLWechatmoments.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mContext.getWindow().setAttributes(lp);
        super.showAtLocation(parent, gravity, x, y);
    }

    private void clear() {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mContext.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                clear();
                dismiss();
                break;
        }
    }
}
