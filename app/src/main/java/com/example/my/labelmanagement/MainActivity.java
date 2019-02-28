package com.example.my.labelmanagement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.my.labelmanagement.adapter.MenuAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

/**
 * 主界面
 * Created by 张智超 on 2019/02/25
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;//侧滑样式
    private TextView mToolbarTitle;//标题
    private ImageView mIvMenu;//侧滑菜单按钮
    private NavigationView mNvMenu;//侧边菜单栏
    private ScrollIndicatorView mIndicator;//横向菜单
    private ViewPager mViewPager;//页面

    private IndicatorViewPager indicatorViewPager;
    private MenuAdapter adapter;

    private ToggleButton mBtnVoice;//声音
    private ToggleButton mBtnShake;//震动
    private TextView mMenuGuide;//新手指南
    private TextView mMenuUpdate;//版本更新
    private TextView mMenuHelp;//帮助
    private TextView mMenuAgreement;//用户协议


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带标题栏
        setContentView(R.layout.activity_main);

        initView();
        initDate();
    }

    // 初始化控件id
    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mNvMenu = (NavigationView) findViewById(R.id.mNvMenu);
        mIndicator = (ScrollIndicatorView) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        // 加载左侧菜单栏布局
        View headerView = mNvMenu.inflateHeaderView(R.layout.item_left_menu);
        // 获取菜单栏中的控件
        mBtnVoice = headerView.findViewById(R.id.btn_voice);

        mBtnShake = headerView.findViewById(R.id.btn_shake);

        mMenuGuide = headerView.findViewById(R.id.menu_guide);
        mMenuGuide.setOnClickListener(new onListener());//监听点击事件
        mMenuHelp = headerView.findViewById(R.id.menu_help);
        mMenuHelp.setOnClickListener(new onListener());
        mMenuUpdate = headerView.findViewById(R.id.menu_update);
        mMenuUpdate.setOnClickListener(new onListener());
        mMenuAgreement = headerView.findViewById(R.id.menu_agreement);
        mMenuAgreement.setOnClickListener(new onListener());
    }

    private void initDate() {
        mToolbarTitle.setText(R.string.app_name);
        mIvMenu.setOnClickListener(new onListener());

        float unSelectSize = 14;//未选中字体大小
        float selectSize = 16;//选中字体大小
        mIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.parseColor("#ffffff"),Color.parseColor("#DCE8FC")).setSize(selectSize,unSelectSize));
        // 设置字体下面的bar样式
        ColorBar scrollBar = new ColorBar(this, Color.parseColor("#ffffff"), 6);
        scrollBar.setWidth(110);
        mIndicator.setScrollBar(scrollBar);
        // 设置预加载页面数量为3
        mViewPager.setOffscreenPageLimit(3);
        indicatorViewPager = new IndicatorViewPager(mIndicator, mViewPager);
        // 加载适配器
        adapter = new MenuAdapter(getSupportFragmentManager(), this);
        indicatorViewPager.setAdapter(adapter);


    }

    // 独立类实现按钮监听事件
    public class onListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_menu:
                    mDrawerLayout.openDrawer(GravityCompat.START);//显示侧边栏
                    break;
                case R.id.menu_guide:
                    openAct(NothingActivity.class);//新手指南页面
                    break;
                case R.id.menu_update:
                    openAct(NothingActivity.class);//版本更新页面
                    break;
                case R.id.menu_help:
                    openAct(NothingActivity.class);//帮助页面
                    break;
                case R.id.menu_agreement:
                    openAct(NothingActivity.class);//用户协议页面
                    break;

            }
        }
    }

    /**
     * 打开一个activity页面，不传递数据
     *
     * @param actClass 传入要跳转的activity
     */
    public void openAct(Class<?> actClass) {
        Intent intent = new Intent(this, actClass);
        startActivity(intent);
    }

}
