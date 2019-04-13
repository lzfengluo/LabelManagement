package com.example.my.labelmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.my.labelmanagement.adapter.WriteAdapter;
import com.example.my.labelmanagement.app.BaseNfcActivity;
import com.example.my.labelmanagement.listener.OnNewIntentListener;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

/**
 * 写入界面
 *
 * @author 张智超
 * @date 2019/4/13
 */
public class WriteActivity extends BaseNfcActivity {

    private ImageView mIvMenu;
    private TextView mMToolbarTitle;
    private ScrollIndicatorView mIndicator;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initView();
        initData();
    }

    private void initData() {
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteActivity.this.finish();
            }
        });
        mMToolbarTitle.setText(R.string.write_title_one);

        float unSelectSize = 14;
        float selectSize = 16;
        mIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.parseColor("#ffffff")
                , Color.parseColor("#deebeb")).setSize(selectSize, unSelectSize));
        ColorBar scrollBar = new ColorBar(this, Color.parseColor("#ffffff"), 6);
        scrollBar.setWidth(110);
        mIndicator.setScrollBar(scrollBar);
        mViewPager.setOffscreenPageLimit(3);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(mIndicator, mViewPager);
        WriteAdapter adapter = new WriteAdapter(getSupportFragmentManager(), this);
        indicatorViewPager.setAdapter(adapter);
    }

    private void initView() {
        LinearLayout mAppBar = (LinearLayout) findViewById(R.id.app_bar);
        mIvMenu = findViewById(R.id.iv_menu);
        mMToolbarTitle = findViewById(R.id.mToolbarTitle);
        mIndicator = findViewById(R.id.indicator);
        mViewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("zm", "onNewIntent: ");
        callBack(intent);
    }

    private OnNewIntentListener onNewIntentListener = null;

    public void setOnNewIntentListener(OnNewIntentListener onNewIntentListener) {
        this.onNewIntentListener = onNewIntentListener;
    }

    public void removeOnNewIntentListener() {
        this.onNewIntentListener = null;
    }

    private void callBack(Intent intent) {
        if (onNewIntentListener != null) {
            onNewIntentListener.sendIntent(intent);
        }
    }
}
