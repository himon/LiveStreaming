package com.futang.livestreaming.ui.activity.account;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.futang.livestreaming.R;
import com.futang.livestreaming.app.LiveApplication;
import com.futang.livestreaming.data.entity.UserEntity;
import com.futang.livestreaming.ui.activity.MainActivity;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.base.ToolbarActivity;
import com.futang.livestreaming.ui.module.RegisterNextActivityModule;
import com.futang.livestreaming.ui.presenter.RegisterNextActivityPresenter;
import com.futang.livestreaming.ui.view.IRegisterNextView;
import com.futang.livestreaming.util.callback.UserCallback;
import com.futang.livestreaming.util.loader.GlideImageLoader;
import com.futang.livestreaming.util.loader.GlidePauseOnScrollListener;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;
import okhttp3.Request;

public class RegisterNextActivity extends ToolbarActivity implements IRegisterNextView, View.OnClickListener {


    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;

    @Bind(R.id.et_nick_name)
    EditText mEtNickName;
    @Bind(R.id.iv_nick_name_delete)
    ImageView mIvNickNameDel;
    @Bind(R.id.et_id)
    EditText mEtID;
    @Bind(R.id.iv_id_delete)
    ImageView mIvIDDel;
    @Bind(R.id.et_sex)
    EditText mEtSex;
    @Bind(R.id.iv_sex_delete)
    ImageView mIvSexDel;
    @Bind(R.id.btn_next)
    Button mBtnNext;
    @Bind(R.id.ll_user_icon)
    LinearLayout mLLUserIcon;
    @Bind(R.id.iv_user_icon)
    ImageView mIvUserIcon;

    @Inject
    RegisterNextActivityPresenter mPresenter;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_next, R.string.title_user_info, MODE_BACK);
        ButterKnife.bind(this);

        initEvent();
    }

    private void initEvent() {
        mBtnNext.setOnClickListener(this);
        mLLUserIcon.setOnClickListener(this);
    }

    @Override
    protected void setupActivityComponent() {
        LiveApplication.get(this).getUserComponent().plus(new RegisterNextActivityModule(this)).inject(this);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_icon:
                galleryInit();
                break;
            case R.id.btn_next:
                mPresenter.nextStep(mFile, mEtNickName.getText().toString(), mEtSex.getText().toString(), mEtID.getText().toString(), new MyStringCallback());
                break;
        }
    }

    private void galleryInit() {
        //公共配置都可以在application中配置，这里只是为了代码演示而写在此处
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        //设置图片加载方式
        ImageLoader imageLoader = new GlideImageLoader();
        PauseOnScrollListener pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);
        //单选或多选
        //int maxSize = 8;
        //functionConfigBuilder.setMutiSelectMaxSize(maxSize);
        final boolean mutiSelect = false;
        //裁剪宽高
        //functionConfigBuilder.setCropWidth(width);
        //functionConfigBuilder.setCropHeight(height);
        //强制裁剪
        functionConfigBuilder.setForceCrop(true);
        //强制裁剪后可编辑
        functionConfigBuilder.setForceCropEdit(true);
        //functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        final FunctionConfig functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(false)
                .build();
        GalleryFinal.init(coreConfig);

        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles("打开相册(Open Gallery)", "拍照(Camera)")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                if (mutiSelect) {
                                    GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                } else {
                                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                }
                                break;
                            case 1:
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();

    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                PhotoInfo photoInfo = resultList.get(0);
                mLLUserIcon.setVisibility(View.GONE);
                mIvUserIcon.setVisibility(View.VISIBLE);
                Glide.with(mIvUserIcon.getContext())
                        .load(photoInfo.getPhotoPath())
                        .into(mIvUserIcon);
                mFile = new File(photoInfo.getPhotoPath());
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    public class MyStringCallback extends UserCallback
    {
        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);
            //setTitle("loading...");
        }

        @Override
        public void onAfter()
        {
            super.onAfter();
            //setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e)
        {
            System.out.print(e);
        }

        @Override
        public void onResponse(UserEntity response) {
            UserEntity.BodyBean user = response.getBody();
        }

        @Override
        public void inProgress(float progress)
        {
//            Log.e(TAG, "inProgress:" + progress);
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }
}
