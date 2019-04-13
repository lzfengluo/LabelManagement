package com.example.my.labelmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户协议界面
 * Created by 张智超_ on 2019/1/22.
 * Email 981547125@qq.com
 */
public class UserAgreementActivity extends Activity {

    private ImageView mIvMenu;
    private TextView mToolbarTitle;
    private TextView agmTitle;
    private TextView agmText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        initView();
        initData();
    }

    private void initView() {
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        agmTitle = (TextView) findViewById(R.id.agm_title);
        agmText = (TextView) findViewById(R.id.agm_text);
    }

    private void initData() {
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAgreementActivity.this.finish();
            }
        });
        mToolbarTitle.setText(R.string.menu_agreement);


    }



}
