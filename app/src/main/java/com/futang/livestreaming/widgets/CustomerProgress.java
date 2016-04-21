package com.futang.livestreaming.widgets;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.futang.livestreaming.R;
import com.futang.livestreaming.util.JumpingBeans;
import com.pnikosis.materialishprogress.ProgressWheel;


/**
 * ProgressWheel: compile 'com.pnikosis:materialish-progress:1.7'
 * https://github.com/pnikosis/materialish-progress
 */
public class CustomerProgress extends ProgressDialog {

    private String mMsg;
    private TextView mTvMsg;
    private JumpingBeans jumpingBeans;
    private ProgressWheel progressWheel;

    public CustomerProgress(Context context, String msg) {
        super(context);
        mMsg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        mTvMsg = (TextView) findViewById(R.id.tv_pro);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        if (!mMsg.equals("") && mMsg != null) {
            mTvMsg.setText(mMsg);
        }
        jumpingBeans = new JumpingBeans.Builder().appendJumpingDots(
                mTvMsg).build();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        jumpingBeans.stopJumping();
        progressWheel.stopSpinning();
    }

}
