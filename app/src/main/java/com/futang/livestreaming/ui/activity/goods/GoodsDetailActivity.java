package com.futang.livestreaming.ui.activity.goods;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import com.futang.livestreaming.R;
import com.futang.livestreaming.ui.base.BaseActivity;
import com.futang.livestreaming.ui.base.ToolbarActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import butterknife.ButterKnife;

public class GoodsDetailActivity extends ToolbarActivity {

    @Override
    protected void setupActivityComponent() {

    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_goods_detail, R.string.title_goods, MODE_BACK);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
    }

    @Override
    protected void setUpData() {

    }
}
