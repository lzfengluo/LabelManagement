package com.example.my.labelmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 新手指南界面
 * Created by 张智超_ on 2019/1/18.
 * Email 981547125@qq.com
 */
public class NewbieGuideActivity extends Activity {

    private ImageView mIvMenu;
    private TextView mToolbarTitle;
    private TextView NewbieTitle;
    private TextView NewbieModel;
    private ImageView NewbieImg;
    private TextView NewbieDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbie_guide);
        initView();
        initData();
    }

    private void initView(){
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        NewbieTitle = (TextView)findViewById(R.id.newbie_title);
        NewbieModel = (TextView)findViewById(R.id.newbie_model);
        NewbieImg = (ImageView) findViewById(R.id.newbie_img);
        NewbieDesc = (TextView)findViewById(R.id.newbie_desc);
    }

    private void initData(){
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuideActivity.this.finish();
            }
        });
        mToolbarTitle.setText(R.string.menu_guide);
    }
}
