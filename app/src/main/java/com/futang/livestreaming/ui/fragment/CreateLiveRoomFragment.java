package com.futang.livestreaming.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.futang.livestreaming.R;

import butterknife.Bind;
import butterknife.ButterKnife;


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

    public CreateLiveRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_live_room, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mBtnStart.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_live:

                break;
            case R.id.iv_close:
                break;
        }
    }
}
