package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.example.my.labelmanagement.adapter.FeatureAdapter;
import com.example.my.labelmanagement.adapter.MyAsyncListUtil;

import java.lang.reflect.Field;

/**
 * IT设备特征数据表 信息展示界面
 *
 * @author 张智超
 * @date 2019/4/14
 */
public class FeatureActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvMenu;
    private TextView mMToolbarTitle;
    private RecyclerView mRecyclerView;
    private ImageView mIvRefresh;
    private ImageView mIvMore;
    private PopupWindow popupWindow;
    private View contentView;
    private ConstraintLayout mLlNull;
    private TextView mTvFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);
        initView();
        initData();
    }

    private void initData() {
        mIvMore.setVisibility(View.VISIBLE);
        mIvRefresh.setVisibility(View.VISIBLE);
        mIvRefresh.setOnClickListener(this);
        mIvMore.setOnClickListener(this);
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeatureActivity.this.finish();
            }
        });
        mMToolbarTitle.setText(R.string.file_feature_title);
        mTvFile.setText(R.string.file_feature_address);
        setRVAdapter();
        initPopupWindow();
    }

    private void setRVAdapter() {
        MyAsyncListUtil myAsyncListUtil = new MyAsyncListUtil(mRecyclerView);
        FeatureAdapter featureAdapter = new FeatureAdapter(this, myAsyncListUtil, mLlNull);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(featureAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (featureAdapter.getItemCount() == 0) {
//            mLlNull.setVisibility(View.VISIBLE);
//        } else {
//            mLlNull.setVisibility(View.GONE);
//        }
    }

    private void initView() {
        mIvMenu = findViewById(R.id.iv_menu);
        mMToolbarTitle = findViewById(R.id.mToolbarTitle);
        mRecyclerView = findViewById(R.id.recyclerView);
        mIvRefresh = findViewById(R.id.iv_refresh);
        mIvMore = findViewById(R.id.iv_more);
        mLlNull = findViewById(R.id.ll_null);
        mTvFile = findViewById(R.id.tv_file);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_more:
                showPopWindow();
                break;
            case R.id.iv_refresh:
                MyAsyncListUtil myAsyncListUtil = new MyAsyncListUtil(mRecyclerView);
                FeatureAdapter featureAdapter = new FeatureAdapter(this, myAsyncListUtil, mLlNull);
                mRecyclerView.swapAdapter(featureAdapter, false);
                break;
            default:
                break;
        }
    }


    @SuppressLint("InflateParams")
    private void initPopupWindow() {
        contentView = LayoutInflater.from(this).inflate(R.layout.popup_feature, null, false);
        //实例化PopupWindow并设置宽高
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //进入退出的动画
        popupWindow.setTouchable(true);
        fitPopupWindowOverStatusBar(true);
        TextView mTvHistory = contentView.findViewById(R.id.tv_history);
        TextView mTvCancel = contentView.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        mTvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureActivity.this, HistoryActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }

    private void showPopWindow() {
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    public void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { //利用反射重新设置mLayoutInScreen的值，当mLayoutInScreen为true时则PopupWindow覆盖全屏。
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(popupWindow, needFullScreen);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
