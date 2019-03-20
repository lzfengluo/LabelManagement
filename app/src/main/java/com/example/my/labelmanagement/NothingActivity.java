package com.example.my.labelmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.my.labelmanagement.view.MarqueeTextView;

/**
 * 尚未开发界面提示
 * @author 张智超
 */
public class NothingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error);
        initView();
    }

    private void initView(){
        ImageView ivBack = findViewById(R.id.iv_back);
        MarqueeTextView mTitle = findViewById(R.id.mToolbarTitle);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText(R.string.app_name);
    }
}
