package com.futang.livestreaming.ui.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.futang.livestreaming.data.RepositoriesManager;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.activity.account.RegisterNextActivity;
import com.futang.livestreaming.ui.view.IRegisterNextView;
import com.futang.livestreaming.util.Observer.SimpleObserver;

import java.io.File;

import cn.finalteam.toolsfinal.io.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrator on 2016/4/12.
 */
public class RegisterNextActivityPresenter {

    private IRegisterNextView mIRegisterNextView;
    private RepositoriesManager mRepositoriesManager;

    public RepositoriesManager getmRepositoriesManager() {
        return mRepositoriesManager;
    }

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

    public void nextStep(File file, String userName, String sex, String loginId) {
        mRepositoriesManager.nextStep(file, userName, sex, loginId).subscribe(new SimpleObserver<UserEntity>(){
            @Override
            public void onNext(UserEntity userEntity) {
                super.onNext(userEntity);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
