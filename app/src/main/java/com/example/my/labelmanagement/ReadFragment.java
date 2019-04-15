package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.serialport.SerialPort;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.example.my.labelmanagement.been.ReadRackBeen;
import com.example.my.labelmanagement.been.ReadTagBeen;
import com.example.my.labelmanagement.listener.OnNewIntentListener;
import com.example.my.labelmanagement.utils.Crc16Utils;
import com.example.my.labelmanagement.utils.DataUtils;
import com.example.my.labelmanagement.utils.MySharedPreferences;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.google.gson.Gson;
import com.shizhefei.fragment.LazyFragment;

import java.nio.charset.Charset;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 读取界面
 *
 * @author 张智超
 * @date 2019/02/25
 */
public class ReadFragment extends LazyFragment implements OnNewIntentListener {
    private MainActivity mContext;
    private ImageView mImgReadcardGif;
    private ScrollView mScShowmsg;
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
    private ScrollView mScShowRack;
    private TextView mTvUserName;
    private TextView mTvPwd;
    private TextView mTvDataCenter;
    private TextView mTvDataCenterName;
    private TextView mTvRow;
    private TextView mTvColumn;
    private TextView mTvIpv4Address;
    private TextView mTvIpv4Mask;
    private TextView mTvIpv4Gateway;
    private TextView mTvIpv6Address;
    private TextView mTvIpv6Prefix;
    private TextView mTvIpv6Gateway;
    private TextView mTvRatedPowe;
    private TextView mTvStatus;
    private TextView mTvMessage;
    private LinearLayout mLlGif;
    private TextView mTvAction;
    private LinearLayout mLlMessage;
    private View mViewMessage;

    public ReadFragment() {
    }

    public ReadFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_read);
        initView();
        initData();
    }


    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        mLlGif.setVisibility(View.VISIBLE);
        mScShowmsg.setVisibility(View.GONE);
        mScShowRack.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (MainActivity) context;
    }

    private void initData() {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(this).load(R.drawable.readcar).apply(options).into(mImgReadcardGif);
        mContext.setOnNewIntentListener(this);
    }

    private void initView() {
        LinearLayout mActivityMain = (LinearLayout) findViewById(R.id.activity_main);
        mImgReadcardGif = (ImageView) findViewById(R.id.img_readcard_gif);
        mScShowmsg = (ScrollView) findViewById(R.id.sc_showmsg);
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
        mScShowRack = (ScrollView) findViewById(R.id.sc_show_rack);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvPwd = (TextView) findViewById(R.id.tv_pwd);
        mTvDataCenter = (TextView) findViewById(R.id.tv_data_center);
        mTvDataCenterName = (TextView) findViewById(R.id.tv_data_center_name);
        mTvRow = (TextView) findViewById(R.id.tv_row);
        mTvColumn = (TextView) findViewById(R.id.tv_column);
        mTvIpv4Address = (TextView) findViewById(R.id.tv_ipv4_address);
        mTvIpv4Mask = (TextView) findViewById(R.id.tv_ipv4_mask);
        mTvIpv4Gateway = (TextView) findViewById(R.id.tv_ipv4_gateway);
        mTvIpv6Address = (TextView) findViewById(R.id.tv_ipv6_address);
        mTvIpv6Prefix = (TextView) findViewById(R.id.tv_ipv6_prefix);
        mTvIpv6Gateway = (TextView) findViewById(R.id.tv_ipv6_gateway);
        mTvRatedPowe = (TextView) findViewById(R.id.tv_rated_powe);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mLlGif = (LinearLayout) findViewById(R.id.ll_gif);
        mTvAction = (TextView) findViewById(R.id.tv_action);
        mLlMessage = (LinearLayout) findViewById(R.id.ll_message);
        mViewMessage = findViewById(R.id.view_message);
    }

    @SuppressLint("InflateParams")
    @Override
    public void sendIntent(Intent intent) {
        Log.d("zzc","sendIntent : "+intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        mLlGif.setVisibility(View.VISIBLE);
        mScShowmsg.setVisibility(View.GONE);
        mScShowRack.setVisibility(View.GONE);
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
            ToastUtil.customToastView(mContext, R.string.support, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return;
        }
        readTag(tag);
    }


    StringBuffer buffer = new StringBuffer();
    int len;

    @SuppressLint("InflateParams")
    public void readTag(Tag tag) {
        buffer.delete(0, buffer.length());
        MifareUltralight ultralight = MifareUltralight.get(tag);
        String hexString = "";
        try {
            ultralight.connect();
            len = Integer.parseInt(DataUtils.byteToHex(ultralight.readPages(4)[1]), 16);
            for (int i = 6; i <= len; i += 4) {
                byte[] data = ultralight.readPages(i);
                String values = new String(data, Charset.forName("UTF-8"));
                buffer.append(values);
            }
            System.out.println("========len \t" + len);
            byte[] bytes = ultralight.readPages(4);
            hexString = DataUtils.toHexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tw", "readTag: " + e.toString());
        } finally {
            try {
                ultralight.close();
                String newstr = DataUtils.substring(buffer.toString(), 0, len);

                Log.i("tw", "readTag: " + newstr);
                Log.e("ZM", "readTag: " + newstr);
                if (null != newstr && !TextUtils.isEmpty(hexString)) {
                    assert hexString != null;
                    String substring = hexString.substring(4, 8);
                    byte[] newstrBytes = newstr.getBytes(Charset.forName("UTF-8"));

                    String crc = Crc16Utils.getCRC2(newstrBytes);
                    Log.e("ZM", "crc: " + crc);
                    if (!crc.equalsIgnoreCase(substring)) {
                        ToastUtil.customToastView(mContext, R.string.crc_failed, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                        mContext.setPlaySound(false);
                        return;
                    }

                    if (newstr.contains("{\"51\":")) {
                        mLlGif.setVisibility(View.GONE);
                        mScShowmsg.setVisibility(View.GONE);
                        mScShowRack.setVisibility(View.VISIBLE);
                        Gson gs = new Gson();
                        ReadRackBeen readRackBeen = gs.fromJson(newstr, ReadRackBeen.class);
                        Log.i("ZM", "onCreate: " + newstr + "\n" + readRackBeen.toString());
                        String readRackBeen_$66 = readRackBeen.get_$66();
                        if ("1".equals(readRackBeen_$66)) {
                            mTvAction.setText(R.string.fragment_action);
                        } else {
                            mTvAction.setText(R.string.fragment_action2);
                        }
                        mTvUserName.setText(readRackBeen.get_$51());
                        mTvPwd.setText(readRackBeen.get_$52());
                        mTvDataCenter.setText(readRackBeen.get_$53());
                        mTvDataCenterName.setText(readRackBeen.get_$54());
                        mTvRow.setText(readRackBeen.get_$55());
                        mTvColumn.setText(readRackBeen.get_$56());
                        mTvIpv4Address.setText(readRackBeen.get_$57());
                        mTvIpv4Mask.setText(readRackBeen.get_$58());
                        mTvIpv4Gateway.setText(readRackBeen.get_$59());
                        mTvIpv6Address.setText(readRackBeen.get_$60());
                        mTvIpv6Prefix.setText(readRackBeen.get_$61());
                        mTvIpv6Gateway.setText(readRackBeen.get_$62());
                        mTvRatedPowe.setText(readRackBeen.get_$63());
                        String readRackBeen_$64 = readRackBeen.get_$64();
                        if ("0".equals(readRackBeen_$64)) {
                            mTvStatus.setText(R.string.fragment_status_unused);
                            Drawable drawable = getResources().getDrawable(R.mipmap.icons_unused);
                            drawable.setBounds(0, 0, 45, 45);
                            mTvStatus.setCompoundDrawables(drawable, null, null, null);
                            mLlMessage.setVisibility(View.GONE);
                            mViewMessage.setVisibility(View.GONE);

                        } else if ("1".equals(readRackBeen_$64)) {
                            mTvStatus.setText(R.string.fragment_status_failed);
                            Drawable drawable = getResources().getDrawable(R.mipmap.icons_fail);
                            drawable.setBounds(0, 0, 45, 45);
                            mTvStatus.setCompoundDrawables(drawable, null, null, null);
                            mLlMessage.setVisibility(View.VISIBLE);
                            mViewMessage.setVisibility(View.VISIBLE);
                            mTvMessage.setText(readRackBeen.get_$65());
                        } else {
                            mTvStatus.setText(R.string.fragment_status_success);
                            Drawable drawable = getResources().getDrawable(R.mipmap.icons_success);
                            drawable.setBounds(0, 0, 45, 45);
                            mTvStatus.setCompoundDrawables(drawable, null, null, null);
                            mLlMessage.setVisibility(View.GONE);
                            mViewMessage.setVisibility(View.GONE);
                        }

                    } else {
                        mLlGif.setVisibility(View.GONE);
                        mScShowmsg.setVisibility(View.VISIBLE);
                        mScShowRack.setVisibility(View.GONE);
                        Gson gs = new Gson();
                        //把JSON字符串转为对象  
                        ReadTagBeen readTagBeen = gs.fromJson(newstr, ReadTagBeen.class);
                        Log.i("ZM", "onCreate: " + newstr + "\n" + readTagBeen.toString());
                        String readTagBeen_sn = readTagBeen.get_$4();
                        mTvSn.setText(readTagBeen_sn);
                        mTvDeviceModle.setText(readTagBeen.get_$1());
                        mTvDeviceType.setText(readTagBeen.get_$2());
                        mTvHeight.setText(readTagBeen.get_$6());
                        mTvVendor.setText(readTagBeen.get_$3());
                        mTvShengmingzhouqi.setText(readTagBeen.get_$7());
                        mTvFirstTime.setText(readTagBeen.get_$8());
                        mTvWeight.setText(readTagBeen.get_$9());
                        mTvRatePower.setText(readTagBeen.get_$10());
                        mTvOwner.setText(readTagBeen.get_$11());
                        mTvBujianNum.setText(readTagBeen.get_$5());

                        serialPortSend(readTagBeen_sn);
                    }

                    boolean shake = MySharedPreferences.getBoolean("shake", true);
                    if (shake) {
                        mContext.vibrate(mContext, 500);
                    }
                    boolean sound = MySharedPreferences.getBoolean("sound", true);
                    if (sound) {
                        mContext.setPlaySound(true);
                    }

                }

            } catch (Exception e) {
                ToastUtil.customToastView(mContext, e.toString(), Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            }
        }

    }


    private void serialPortSend(final String data) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) {
                try {
                    SerialPort serialPort = new SerialPort();
                    serialPort.OpenSerial("/dev/ttyUSB0", 9600);
                    int fd = serialPort.getFd();
                    serialPort.clearPortBuf(fd);
                    serialPort.WriteSerialByte(fd, data.getBytes());
                    Thread.sleep(200);
                    serialPort.CloseSerial(fd);
                    e.onNext(1);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.disposable = d;
                    }

                    @SuppressLint("InflateParams")
                    @Override
                    public void onNext(Integer value) {
                        ToastUtil.customToastView(getActivity(), getString(R.string.serport_success), Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_toast, null));
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }
}
