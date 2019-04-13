package com.example.my.labelmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 具体问题界面
 * Created by 张智超_ on 2019/1/22.
 * Email 981547125@qq.com
 */
public class HelpQuestionActivity extends Activity {

    private ImageView mIvMenu;
    private TextView mToolbarTitle;
    private TextView queTitle;
    private TextView queWay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_question);
        initView();
        initData();
    }

    private void initView() {
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        queTitle = (TextView) findViewById(R.id.que_title);
        queWay = (TextView) findViewById(R.id.que_way);
    }

    private void initData() {
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpQuestionActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int position = bundle.getInt("position");
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        mToolbarTitle.setText(title);
        queTitle.setText(title);
        queWay.setText(content);
    }



}
