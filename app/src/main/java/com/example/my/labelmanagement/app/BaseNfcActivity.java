package com.example.my.labelmanagement.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.my.labelmanagement.utils.MySharedPreferences;
import com.example.my.labelmanagement.utils.PlaySoundPoolUtils;
import com.speedata.libutils.SharedXmlUtil;

/**
 * 子类在onNewIntent方法中进行NFC标签相关操作。
 * launchMode设置为singleTop或singelTask，保证Activity的重用唯一
 * 在onNewIntent方法中执行intent传递过来的Tag数据
 * @author My_PC
 */
public class BaseNfcActivity extends AppCompatActivity {
    protected NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    public PlaySoundPoolUtils playSoundPoolUtils;

    /**
     * onCreat->onStart->onResume->onPause->onStop->onDestroy
     * 启动Activity，界面可见时.
     */
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉自带标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("zm", "onCreate: ");
        MySharedPreferences.getPreference(this);
        playSoundPoolUtils = PlaySoundPoolUtils.getPlaySoundPoolUtils(this);
        //此处adapter需要重新获取，否则无法获取message
        if (mNfcAdapter == null) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        if (mPendingIntent == null) {
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        }
    }

    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
//        //去掉Activity上面的状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //去掉虚拟按键全屏显示
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        //点击屏幕不再显示
//        this.getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
//                        // bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    @Override
    public void onPause() {
        super.onPause();
        //恢复默认状态
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    public void setPlaySound(boolean isSuccess) {
        boolean read = SharedXmlUtil.getInstance(this, "nfc").read("Voice", true);
        if (read) {
            if (isSuccess) {
                playSoundPoolUtils.playLaser();
            } else {
                playSoundPoolUtils.playError();
            }
        }
    }

    //震动milliseconds毫秒
    public void vibrate(final Activity activity, long milliseconds) {
        boolean shake = SharedXmlUtil.getInstance(this, "nfc").read("Shake", true);
        if (shake) {
            Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(milliseconds);
        }
    }

    //以pattern[]方式震动
    public void vibrate(final Activity activity, long[] pattern, int repeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, repeat);
    }

    //取消震动
    public void virateCancle(final Activity activity) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
    }
}

