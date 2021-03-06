package com.example.my.labelmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.my.labelmanagement.adapter.MenuAdapter;
import com.example.my.labelmanagement.app.BaseNfcActivity;
import com.example.my.labelmanagement.listener.OnNewIntentListener;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.speedata.libutils.SharedXmlUtil;

/**
 * 主界面
 * @author 张智超
 * @date 2019/02/25
 */
public class MainActivity extends BaseNfcActivity {

    private DrawerLayout mDrawerLayout;
    private TextView mToolbarTitle;
    private ImageView mIvMenu;
    private ScrollIndicatorView mIndicator;
    private ViewPager mViewPager;
    /**
     * 声音
     */
    private ToggleButton mBtnVoice;
    /**
     * 震动
     */
    private ToggleButton mBtnShake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉自带标题栏
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    /**
     * 初始化控件id
     */
    private void initView() {
        //侧滑样式
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        //标题
        mToolbarTitle = findViewById(R.id.mToolbarTitle);
        //侧滑菜单按钮
        mIvMenu = findViewById(R.id.iv_menu);
        //侧边菜单栏
        NavigationView mNvMenu = findViewById(R.id.mNvMenu);
        //横向菜单
        mIndicator = findViewById(R.id.indicator);
        //页面
        mViewPager = findViewById(R.id.viewPager);

        // 加载左侧菜单栏布局
        View headerView = mNvMenu.inflateHeaderView(R.layout.item_left_menu);
        // 获取菜单栏中的控件
        //声音
        mBtnVoice = headerView.findViewById(R.id.btn_voice);
        mBtnVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedXmlUtil.getInstance(MainActivity.this, "nfc").write("Voice", true);
                    ToastUtil.showShort(MainActivity.this,getResources().getString(R.string.voice_open));
                } else {
                    SharedXmlUtil.getInstance(MainActivity.this, "nfc").write("Voice", false);
                    ToastUtil.showShort(MainActivity.this,getResources().getString(R.string.voice_close));
                }
            }
        });
        //震动
        mBtnShake = headerView.findViewById(R.id.btn_shake);
        mBtnShake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedXmlUtil.getInstance(MainActivity.this, "nfc").write("Shake", true);
                    ToastUtil.showShort(MainActivity.this,getResources().getString(R.string.shake_open));
                } else {
                    SharedXmlUtil.getInstance(MainActivity.this, "nfc").write("Shake", false);
                    ToastUtil.showShort(MainActivity.this,getResources().getString(R.string.shake_close));
                }
            }
        });

        //新手指南
        TextView mMenuGuide = headerView.findViewById(R.id.menu_guide);
        //监听点击事件
        mMenuGuide.setOnClickListener(new OnListener());
        //帮助
        TextView mMenuHelp = headerView.findViewById(R.id.menu_help);
        mMenuHelp.setOnClickListener(new OnListener());
        //版本更新
        TextView mMenuUpdate = headerView.findViewById(R.id.menu_update);
        mMenuUpdate.setOnClickListener(new OnListener());
        //用户协议
        TextView mMenuAgreement = headerView.findViewById(R.id.menu_agreement);
        mMenuAgreement.setOnClickListener(new OnListener());
    }

    private void initData() {
        mToolbarTitle.setText(R.string.app_name);
        mIvMenu.setOnClickListener(new OnListener());

        //未选中字体大小
        float unSelectSize = 14;
        //选中字体大小
        float selectSize = 16;
        mIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.parseColor("#ffffff"), Color.parseColor("#DCE8FC")).setSize(selectSize, unSelectSize));
        // 设置字体下面的bar样式
        ColorBar scrollBar = new ColorBar(this, Color.parseColor("#ffffff"), 6);
        scrollBar.setWidth(110);
        mIndicator.setScrollBar(scrollBar);
        // 设置预加载页面数量为3
        mViewPager.setOffscreenPageLimit(3);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(mIndicator, mViewPager);
        // 加载适配器
        MenuAdapter adapter = new MenuAdapter(getSupportFragmentManager(), this);
        indicatorViewPager.setAdapter(adapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        // NFC适配器，所有的关于NFC的操作从该适配器进行
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Log.d("zzc", "mNfcAdapter: onResume()");
        if (!ifNFCUse()) {
            Log.d("zzc", "mNfcAdapter: false");
            return;
        }

        boolean read = SharedXmlUtil.getInstance(this, "nfc").read("Voice", true);
        mBtnVoice.setChecked(read);
        boolean shake = SharedXmlUtil.getInstance(this, "nfc").read("Shake", true);
        mBtnShake.setChecked(shake);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("zzc", "onNewIntent: "+intent);
        callBack(intent);
    }
    private OnNewIntentListener onNewIntentListener = null;

    public void setOnNewIntentListener(OnNewIntentListener onNewIntentListener) {
        this.onNewIntentListener = onNewIntentListener;
        Log.d("zzc", "onNewIntentListener: "+onNewIntentListener);
    }

    private void callBack(Intent intent) {
        if (onNewIntentListener != null) {
            onNewIntentListener.sendIntent(intent);
        }
    }


    /**
     * 独立类实现按钮监听事件
     */
    public class OnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_menu:
                    //显示侧边栏
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.menu_guide:
                    //新手指南页面
                    openAct(NewbieGuideActivity.class);
                    break;
                case R.id.menu_update:
                    //版本更新页面
                    openAct(CheckUpdateActivity.class);
                    break;
                case R.id.menu_help:
                    //帮助页面
                    openAct(HelpActivity.class);
                    break;
                case R.id.menu_agreement:
                    //用户协议页面
                    openAct(UserAgreementActivity.class);
                    break;
                default:
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

    /**
     * 检测工作,判断设备的NFC支持情况
     *
     * @return 返回true或false
     */
    protected Boolean ifNFCUse() {
        if (mNfcAdapter == null) {
            Toast.makeText(this, getResources().getString(R.string.nfc_erro2), Toast.LENGTH_SHORT).show();
            MainActivity.this.finish();
            return false;
        }
        if (!mNfcAdapter.isEnabled()) {
            ToastUtil.showShort(this, getResources().getString(R.string.nfc_erro));
            handler.postDelayed(runnable, 3000);
            return false;
        }
        return true;
    }

    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent("android.settings.NFC_SETTINGS"));
        }
    };

    private long mkeyTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.ACTION_DOWN:
                if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                    mkeyTime = System.currentTimeMillis();
                    boolean cn = "CN".equals(getApplicationContext().getResources().getConfiguration().locale.getCountry());
                    if (cn) {
                        Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Click again to exit", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
