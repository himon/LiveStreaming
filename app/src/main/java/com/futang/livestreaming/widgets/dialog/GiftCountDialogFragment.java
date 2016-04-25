package com.futang.livestreaming.widgets.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.futang.livestreaming.R;
import com.futang.livestreaming.util.ToastUtils;

/**
 * Created by lc on 16/4/25.
 */
public class GiftCountDialogFragment extends DialogFragment implements View.OnClickListener {

    private View mView;

    private EditText mEtCount;
    private Button mBtnConfirm;
    private Button mBtnCancel;

    private GiftCountDialogListener mListener;

    //对外开放的接口
    public interface GiftCountDialogListener {

        void confirm(int count);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() instanceof GiftCountDialogListener) {
            mListener = (GiftCountDialogListener) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = inflater.inflate(R.layout.dialog_fragment_gift_count, container);
        initView();
        initData();
        return mView;
    }

    private void initData() {

    }

    private void initEvent() {
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    private void initView() {
        mEtCount = (EditText) mView.findViewById(R.id.et_count);
        mBtnConfirm = (Button) mView.findViewById(R.id.btn_confirm);
        mBtnCancel = (Button) mView.findViewById(R.id.btn_cancel);

        initEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                String s = mEtCount.getText().toString();
                int count = Integer.valueOf(s);
                if (count == 0) {
                    ToastUtils.showToast(getActivity(), "请输入礼物数量!");
                    return;
                }
                mListener.confirm(count);
                dismiss();
                break;
        }
    }
}
