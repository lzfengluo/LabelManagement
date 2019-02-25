package com.example.my.labelmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shizhefei.view.indicator.ScrollIndicatorView;

/**
 * 主界面
 * Created by 张智超 on 2019/02/25
 */
public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;//侧滑样式
    private TextView mToolbarTitle;//标题
    private ImageView mIvMenu;//侧滑菜单按钮
    private NavigationView mNvMenu;//侧边菜单栏
    private ScrollIndicatorView mIndicator;//横向菜单
    private ViewPager mViewPager;//页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDate();
    }

    //    初始化控件id
    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mNvMenu = (NavigationView) findViewById(R.id.mNvMenu);
        mIndicator = (ScrollIndicatorView) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void initDate() {
        mToolbarTitle.setText(R.string.app_name);
        mIvMenu.setOnClickListener(new onListener());
    }

    // 独立类实现按钮监听事件
    public class onListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_menu:
                    mDrawerLayout.openDrawer(GravityCompat.START);//显示侧边栏
                    break;
            }
        }
    }
}
