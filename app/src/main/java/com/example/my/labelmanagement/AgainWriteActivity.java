package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.labelmanagement.app.BaseNfcActivity;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;
import com.example.my.labelmanagement.been.RvMoreAdapterBeanDao;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.utils.Crc16Utils;
import com.example.my.labelmanagement.utils.DataUtils;
import com.example.my.labelmanagement.utils.Math;
import com.example.my.labelmanagement.utils.MySharedPreferences;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.example.my.labelmanagement.view.MarqueeTextView;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


/**
 * 批量写入的某个标签详细信息写入界面
 *
 * @author 张智超_
 * @date 2019/4/16
 * Email 981547125@qq.com
 */
public class AgainWriteActivity extends BaseNfcActivity {

    private ImageView mIvMenu;
    private MarqueeTextView mMToolbarTitle;
    private TextView mTvStatus;
    private TextView mTvSn;
    private TextView mTvDeviceModle;
    private TextView mTvDeviceType;
    private TextView mTvHeight;
    private TextView mTvVendor;
    private TextView mTvShengmingzhouqi;
    private TextView mTvFirstTime;
    private TextView mTvWeight;
    private TextView mTvRatePower;
    private TextView mTvOwner;
    private TextView mTvBujianNum;
    private Button mBtnWrite;
    private Tag tag;
    private TagInfoBean tagInfoBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_again_write);
        initView();
        initData();
    }

    private void initData() {
        String sn = getIntent().getStringExtra("SN");
        tagInfoBean = getIntent().getParcelableExtra("Bean");
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgainWriteActivity.this.finish();
            }
        });
        mMToolbarTitle.setText(sn);

        boolean isWrite = tagInfoBean.getIsWrite();
        if (isWrite) {
            mTvStatus.setText(R.string.writed);
            Drawable drawable = getResources().getDrawable(R.mipmap.yuan_sure);
            drawable.setBounds(0, 0, 32, 32);
            mTvStatus.setCompoundDrawables(drawable, null, null, null);
            mBtnWrite.setText(R.string.write_again);
        } else {
            mTvStatus.setText(R.string.no_write);
            Drawable drawable = getResources().getDrawable(R.mipmap.yuan_cancel);
            drawable.setBounds(0, 0, 32, 32);
            mTvStatus.setCompoundDrawables(drawable, null, null, null);
            mBtnWrite.setText(R.string.write);
        }

        mTvSn.setText(tagInfoBean.getSN());
        mTvDeviceModle.setText(tagInfoBean.getModel());
        mTvDeviceType.setText(tagInfoBean.getType());
        mTvHeight.setText(tagInfoBean.getOccupiedHeight());
        mTvVendor.setText(tagInfoBean.getVendor());
        mTvShengmingzhouqi.setText(tagInfoBean.getLifecycle());
        mTvFirstTime.setText(tagInfoBean.getFirstUse());
        mTvWeight.setText(tagInfoBean.getWeight());
        mTvRatePower.setText(tagInfoBean.getRatedPower());
        mTvOwner.setText(tagInfoBean.getOwner());
        mTvBujianNum.setText(tagInfoBean.getPartNum());

        mBtnWrite.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                if (null == tag) {
                    setPlaySound(false);
                    ToastUtil.customToastView(AgainWriteActivity.this, getString(R.string.near), Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(AgainWriteActivity.this).inflate(R.layout.layout_toast, null));
                } else {
                    mBtnWrite.setEnabled(false);
                    writeTag(tag);
                }
            }
        });

    }

    @SuppressLint("InflateParams")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.contains("MifareUltralight")) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            ToastUtil.customToastView(getApplicationContext(), R.string.support, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_toast, null));
        }
    }

    private void initView() {
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mMToolbarTitle = (MarqueeTextView) findViewById(R.id.mToolbarTitle);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mTvSn = (TextView) findViewById(R.id.tv_sn);
        mTvDeviceModle = (TextView) findViewById(R.id.tv_device_modle);
        mTvDeviceType = (TextView) findViewById(R.id.tv_device_type);
        mTvHeight = (TextView) findViewById(R.id.tv_height);
        mTvVendor = (TextView) findViewById(R.id.tv_vendor);
        mTvShengmingzhouqi = (TextView) findViewById(R.id.tv_shengmingzhouqi);
        mTvFirstTime = (TextView) findViewById(R.id.tv_first_time);
        mTvWeight = (TextView) findViewById(R.id.tv_weight);
        mTvRatePower = (TextView) findViewById(R.id.tv_rate_power);
        mTvOwner = (TextView) findViewById(R.id.tv_owner);
        mTvBujianNum = (TextView) findViewById(R.id.tv_bujian_num);
        mBtnWrite = (Button) findViewById(R.id.btn_write);
    }

    @SuppressLint("InflateParams")
    public void writeTag(Tag tag) {
        ToastUtil.customToastView(this, getString(R.string.write_ing), Toast.LENGTH_SHORT
                , (TextView) LayoutInflater.from(this).inflate(R.layout.layout_toast, null));
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            int index = 5;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("1", mTvDeviceModle.getText().toString().trim());
            jsonObject.put("2", mTvDeviceType.getText().toString().trim());
            jsonObject.put("3", mTvVendor.getText().toString().trim());
            jsonObject.put("4", mTvSn.getText().toString().trim());
            jsonObject.put("5", mTvBujianNum.getText().toString().trim());
            jsonObject.put("6", mTvHeight.getText().toString().trim());
            jsonObject.put("7", mTvShengmingzhouqi.getText().toString().trim());
            jsonObject.put("8", mTvFirstTime.getText().toString().trim());
            jsonObject.put("9", mTvWeight.getText().toString().trim());
            jsonObject.put("10", mTvRatePower.getText().toString().trim());
            jsonObject.put("11", mTvOwner.getText().toString().trim());


            int len = jsonObject.toString().length() % 4;
            List values = Math.getStrList(jsonObject.toString(), 4);
            byte[] resultBytes = null;
            for (int i = 0; i < values.size(); i++) {
                index++;//从page6开始写入数据

                if (values.get(i).toString().length() >= 4) {
                    if (resultBytes == null) {
                        resultBytes = values.get(i).toString().getBytes(Charset.forName("UTF-8"));
                    } else {
                        resultBytes = DataUtils.concatAll(resultBytes, values.get(i).toString().getBytes(Charset.forName("UTF-8")));
                    }
                    ultralight.writePage(index, values.get(i).toString().getBytes(Charset.forName("UTF-8")));
                    System.out.println("=====values \t" + values.get(i).toString() + "\t" + index + "\t" + len);
                }

            }
            String tmp;
            assert resultBytes != null;
            if (len == 1) {
                tmp = jsonObject.toString().substring(jsonObject.toString().length() - 1);
                resultBytes = DataUtils.concatAll(resultBytes, (tmp + "000").getBytes(Charset.forName("UTF-8")));
                ultralight.writePage(index, (tmp + "000").getBytes(Charset.forName("UTF-8")));
            } else if (len == 2) {
                tmp = jsonObject.toString().substring(jsonObject.toString().length() - 2);
                resultBytes = DataUtils.concatAll(resultBytes, (tmp + "00").getBytes(Charset.forName("UTF-8")));
                ultralight.writePage(index, (tmp + "00").getBytes(Charset.forName("UTF-8")));
            } else if (len == 3) {
                tmp = jsonObject.toString().substring(jsonObject.toString().length() - 3);
                resultBytes = DataUtils.concatAll(resultBytes, (tmp + "0").getBytes(Charset.forName("UTF-8")));
                ultralight.writePage(index, (tmp + "0").getBytes(Charset.forName("UTF-8")));
            }


            if (resultBytes != null) {
                String crc = Crc16Utils.getCRC2(jsonObject.toString().getBytes(Charset.forName("UTF-8")));
                byte[] writelens = {(byte) 0x00, (byte) (jsonObject.toString().length())};
                byte[] concatAll = DataUtils.concatAll(writelens, DataUtils.hexStringToByteArray(crc));
                Log.e("ZM", "Page4: " + DataUtils.toHexString(concatAll));
                ultralight.writePage(4, concatAll);
            }

            ultralight.close();
            boolean shake = MySharedPreferences.getBoolean("shake", true);
            if (shake) {
                this.vibrate(this, 500);
            }
            boolean sound = MySharedPreferences.getBoolean("sound", true);
            if (sound) {
                this.setPlaySound(true);
            }
            if (!tagInfoBean.getIsWrite()) {
                RvMoreAdapterBean rvMoreAdapterBean = MyApp.getDaoInstant().getRvMoreAdapterBeanDao().queryBuilder()
                        .where(RvMoreAdapterBeanDao.Properties.Name.eq(tagInfoBean.getFileName())).unique();
                int writeNum = rvMoreAdapterBean.getWriteNum();
                rvMoreAdapterBean.setWriteNum(writeNum + 1);
                MyApp.getDaoInstant().getRvMoreAdapterBeanDao().update(rvMoreAdapterBean);
            }
            tagInfoBean.setIsWrite(true);
            MyApp.getDaoInstant().getTagInfoBeanDao().update(tagInfoBean);
            ToastUtil.customToastView(this, getString(R.string.write_success), Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(this).inflate(R.layout.layout_toast, null));

            mTvStatus.setText(R.string.writed);
            Drawable drawable = getResources().getDrawable(R.mipmap.yuan_sure);
            drawable.setBounds(0, 0, 32, 32);
            mTvStatus.setCompoundDrawables(drawable, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ZM", "Exception: " + e.toString());
            ToastUtil.customToastView(this, getString(R.string.write_failed), Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(this).inflate(R.layout.layout_toast, null));
            boolean shake = MySharedPreferences.getBoolean("shake", true);
            if (shake) {
                vibrate(this, 1000);
            }
            boolean sound = MySharedPreferences.getBoolean("sound", true);
            if (sound) {
                setPlaySound(false);
            }
        } finally {
            mBtnWrite.setEnabled(true);
            try {
                ultralight.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
