package com.futang.livestreaming.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Stay on 22/10/15.
 * Powered by www.stay4it.com
 */
public abstract class BaseFragment extends Fragment {

    protected abstract void setupActivityComponent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
    }
}
