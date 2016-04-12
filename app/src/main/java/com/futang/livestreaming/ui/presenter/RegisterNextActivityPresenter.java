package com.futang.livestreaming.ui.presenter;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.activity.account.RegisterNextActivity;
import com.futang.livestreaming.ui.view.IRegisterNextView;

import java.io.File;



/**
 * Created by Administrator on 2016/4/12.
 */
public class RegisterNextActivityPresenter {

    private IRegisterNextView mIRegisterNextView;
    private RepositoriesManager mRepositoriesManager;

    public RegisterNextActivityPresenter(IRegisterNextView iRegisterNextView, RepositoriesManager repositoriesManager) {
        this.mIRegisterNextView = iRegisterNextView;
        this.mRepositoriesManager = repositoriesManager;
    }

    public void nextStep(File file, String userName, String sex, String loginId, RegisterNextActivity.MyStringCallback callback) {
        if (file != null) {
            if (file.exists()) {
                mRepositoriesManager.nextStep(file, userName, sex, loginId, callback);
            }
        }
    }
}
