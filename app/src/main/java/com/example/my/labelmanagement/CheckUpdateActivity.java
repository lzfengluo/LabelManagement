package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.speedata.libutils.CommonUtils;

/**
 * 版本更新界面
 *
 * @author 张智超_
 * @date 2019/1/21
 * Email 981547125@qq.com
 */
public class CheckUpdateActivity extends Activity {

    private ImageView mIvMenu;
    private TextView mToolbarTitle;
    private TextView checkUpdateVs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_update);
        initView();
        initData();
    }

    private void initView() {
        mIvMenu = findViewById(R.id.iv_menu);
        mToolbarTitle = findViewById(R.id.mToolbarTitle);
        checkUpdateVs = findViewById(R.id.check_update_vs);
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckUpdateActivity.this.finish();
            }
        });
        mToolbarTitle.setText(R.string.menu_update);
        checkUpdateVs.setText(getString(R.string.check_version) + CommonUtils.getAppVersionName(getApplicationContext()));
    }
}
