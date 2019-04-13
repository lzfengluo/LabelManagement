package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.labelmanagement.listener.OnNewIntentListener;
import com.example.my.labelmanagement.utils.CheckUtils;
import com.example.my.labelmanagement.utils.Crc16Utils;
import com.example.my.labelmanagement.utils.DataUtils;
import com.example.my.labelmanagement.utils.Math;
import com.example.my.labelmanagement.utils.MySharedPreferences;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.example.my.labelmanagement.view.ClearEditText;
import com.example.my.labelmanagement.view.MySpinner;

import com.shizhefei.fragment.LazyFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 单个写入 机柜信息界面
 *
 * @author 张智超
 * @date 2019/4/13
 */
public class WriteSingleRackFragment extends LazyFragment implements OnNewIntentListener, View.OnClickListener {
    private WriteActivity mContext;
    private RadioGroup radioGroup;
    private RadioButton rbRead;
    private ClearEditText tvUserName;
    private ClearEditText tvPwd;
    private LinearLayout llMoreInfo;
    private ClearEditText tvDataCenter;
    private ClearEditText tvDataCenterName;
    private ClearEditText tvRow;
    private ClearEditText tvColumn;
    private ClearEditText tvIpv4Address;
    private ClearEditText tvIpv4Mask;
    private ClearEditText tvIpv4Gateway;
    private ClearEditText tvIpv6Address;
    private ClearEditText tvIpv6Prefix;
    private ClearEditText tvIpv6Gateway;
    private ClearEditText tvRatedPowe;
    private MySpinner spinnerType;
    private ClearEditText tvMessage;
    private Button btnWrite;
    private Tag tag;

    public WriteSingleRackFragment() {
    }

    public WriteSingleRackFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_write_single_rack);
        initView();
        initData();
    }


    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        mContext.setOnNewIntentListener(this);
    }

    private void initData() {
        btnWrite.setOnClickListener(this);
        textWatcher();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_read:
                        llMoreInfo.setVisibility(View.GONE);
                        break;
                    case R.id.rb_write:
                        llMoreInfo.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                setBtnEnable();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (WriteActivity) context;
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbRead = (RadioButton) findViewById(R.id.rb_read);
        RadioButton rbWrite = (RadioButton) findViewById(R.id.rb_write);
        tvUserName = (ClearEditText) findViewById(R.id.tv_user_name);
        tvPwd = (ClearEditText) findViewById(R.id.tv_pwd);
        llMoreInfo = (LinearLayout) findViewById(R.id.ll_more_info);
        tvDataCenter = (ClearEditText) findViewById(R.id.tv_data_center);
        tvDataCenterName = (ClearEditText) findViewById(R.id.tv_data_center_name);
        tvRow = (ClearEditText) findViewById(R.id.tv_row);
        tvColumn = (ClearEditText) findViewById(R.id.tv_column);
        tvIpv4Address = (ClearEditText) findViewById(R.id.tv_ipv4_address);
        tvIpv4Mask = (ClearEditText) findViewById(R.id.tv_ipv4_mask);
        tvIpv4Gateway = (ClearEditText) findViewById(R.id.tv_ipv4_gateway);
        tvIpv6Address = (ClearEditText) findViewById(R.id.tv_ipv6_address);
        tvIpv6Prefix = (ClearEditText) findViewById(R.id.tv_ipv6_prefix);
        tvIpv6Gateway = (ClearEditText) findViewById(R.id.tv_ipv6_gateway);
        tvRatedPowe = (ClearEditText) findViewById(R.id.tv_rated_powe);
        spinnerType = (MySpinner) findViewById(R.id.spinner_type);
        tvMessage = (ClearEditText) findViewById(R.id.tv_message);
        btnWrite = (Button) findViewById(R.id.btn_write);
        setBtnEnable();
        LinearLayout mLlUserName = (LinearLayout) findViewById(R.id.ll_user_name);
        mLlUserName.setOnClickListener(this);
        LinearLayout mLlPwd = (LinearLayout) findViewById(R.id.ll_pwd);
        mLlPwd.setOnClickListener(this);
        LinearLayout mLlDataCenter = (LinearLayout) findViewById(R.id.ll_data_center);
        mLlDataCenter.setOnClickListener(this);
        LinearLayout mLlDataCenterName = (LinearLayout) findViewById(R.id.ll_data_center_name);
        mLlDataCenterName.setOnClickListener(this);
        LinearLayout mLlRow = (LinearLayout) findViewById(R.id.ll_row);
        mLlRow.setOnClickListener(this);
        LinearLayout mLlColumn = (LinearLayout) findViewById(R.id.ll_column);
        mLlColumn.setOnClickListener(this);
        LinearLayout mLlIpv4Address = (LinearLayout) findViewById(R.id.ll_ipv4_address);
        mLlIpv4Address.setOnClickListener(this);
        LinearLayout mLlIpv4Mask = (LinearLayout) findViewById(R.id.ll_ipv4_mask);
        mLlIpv4Mask.setOnClickListener(this);
        LinearLayout mLlIpv4Gateway = (LinearLayout) findViewById(R.id.ll_ipv4_gateway);
        mLlIpv4Gateway.setOnClickListener(this);
        LinearLayout mLlIpv6Address = (LinearLayout) findViewById(R.id.ll_ipv6_address);
        mLlIpv6Address.setOnClickListener(this);
        LinearLayout mLlIpv6Prefix = (LinearLayout) findViewById(R.id.ll_ipv6_prefix);
        mLlIpv6Prefix.setOnClickListener(this);
        LinearLayout mLlIpv6Gateway = (LinearLayout) findViewById(R.id.ll_ipv6_gateway);
        mLlIpv6Gateway.setOnClickListener(this);
        LinearLayout mLlRatedPower = (LinearLayout) findViewById(R.id.ll_rated_power);
        mLlRatedPower.setOnClickListener(this);
        LinearLayout mLlSpinnerType = (LinearLayout) findViewById(R.id.ll_spinner_type);
        mLlSpinnerType.setOnClickListener(this);
        LinearLayout mLlMessage = (LinearLayout) findViewById(R.id.ll_message);
        mLlMessage.setOnClickListener(this);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_write:
                if (!checkInput()) {
                    return;
                }
                if (null == tag) {
                    mContext.setPlaySound(false);
                    ToastUtil.customToastView(mContext, getString(R.string.near), Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    writeTag(tag);
                }
                break;

            case R.id.ll_user_name:
                getFocusable(tvUserName);
                break;
            case R.id.ll_pwd:
                getFocusable(tvPwd);
                break;
            case R.id.ll_data_center:
                getFocusable(tvDataCenter);
                break;
            case R.id.ll_data_center_name:
                getFocusable(tvDataCenterName);
                break;
            case R.id.ll_row:
                getFocusable(tvRow);
                break;
            case R.id.ll_column:
                getFocusable(tvColumn);
                break;
            case R.id.ll_ipv4_address:
                getFocusable(tvIpv4Address);
                break;
            case R.id.ll_ipv4_mask:
                getFocusable(tvIpv4Mask);
                break;
            case R.id.ll_ipv4_gateway:
                getFocusable(tvIpv4Gateway);
                break;
            case R.id.ll_ipv6_address:
                getFocusable(tvIpv6Address);
                break;
            case R.id.ll_ipv6_prefix:
                getFocusable(tvIpv6Prefix);
                break;
            case R.id.ll_ipv6_gateway:
                getFocusable(tvIpv6Gateway);
                break;
            case R.id.ll_rated_power:
                getFocusable(tvRatedPowe);
                break;
            case R.id.ll_message:
                getFocusable(tvMessage);
                break;

            case R.id.ll_spinner_type:
                spinnerType.performClick();
                break;
            default:
                break;
        }
    }


    @SuppressLint("InflateParams")
    @Override
    public void sendIntent(Intent intent) {
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
            ToastUtil.customToastView(mContext, R.string.support, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
        }
    }


    @SuppressLint("InflateParams")
    private boolean checkInput() {
        tvUserName.setTextColor(Color.BLACK);
        tvPwd.setTextColor(Color.BLACK);
        tvDataCenter.setTextColor(Color.BLACK);
        tvDataCenterName.setTextColor(Color.BLACK);
        tvRow.setTextColor(Color.BLACK);
        tvColumn.setTextColor(Color.BLACK);
        tvIpv4Address.setTextColor(Color.BLACK);
        tvIpv4Mask.setTextColor(Color.BLACK);
        tvIpv4Gateway.setTextColor(Color.BLACK);
        tvIpv6Address.setTextColor(Color.BLACK);
        tvIpv6Prefix.setTextColor(Color.BLACK);
        tvIpv6Gateway.setTextColor(Color.BLACK);
        tvRatedPowe.setTextColor(Color.BLACK);
        tvMessage.setTextColor(Color.BLACK);

        String userName = Objects.requireNonNull(tvUserName.getText()).toString();
        if (!CheckUtils.stringCheck(userName)) {
            tvUserName.setTextColor(Color.RED);
            ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }
        String pwd = Objects.requireNonNull(tvPwd.getText()).toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            ToastUtil.customToastView(mContext, R.string.write_must, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }

        if (rbRead.isChecked()) {
            return true;
        }

        String dataCenter = Objects.requireNonNull(tvDataCenter.getText()).toString();
        if (!CheckUtils.stringCheck(dataCenter)) {
            tvDataCenter.setTextColor(Color.RED);
            ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }
        String dataCenterName = Objects.requireNonNull(tvDataCenterName.getText()).toString();
        if (!CheckUtils.stringCheck(dataCenterName)) {
            tvDataCenterName.setTextColor(Color.RED);
            ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }
        String rowStr = Objects.requireNonNull(tvRow.getText()).toString();
        if (!TextUtils.isEmpty(rowStr)) {
            int row = Integer.parseInt(rowStr);
            if (row < 1 || row > 500) {
                tvRow.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.num_err_500, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String columnStr = Objects.requireNonNull(tvColumn.getText()).toString();
        if (!TextUtils.isEmpty(columnStr)) {
            int column = Integer.parseInt(columnStr);
            if (column < 1 || column > 500) {
                tvColumn.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.num_err_500, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        if (TextUtils.isEmpty(dataCenter) || TextUtils.isEmpty(dataCenterName) || TextUtils.isEmpty(rowStr) || TextUtils.isEmpty(columnStr)) {
            ToastUtil.customToastView(mContext, R.string.write_must, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }

        String ipv4Address = Objects.requireNonNull(tvIpv4Address.getText()).toString();
        if (!TextUtils.isEmpty(ipv4Address)) {
            if (!CheckUtils.ipCheck(ipv4Address)) {
                tvIpv4Address.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String mask = Objects.requireNonNull(tvIpv4Mask.getText()).toString();
        if (!TextUtils.isEmpty(mask)) {
            int ipv4Mask = Integer.parseInt(mask);
            if (ipv4Mask < 1 || ipv4Mask > 32) {
                tvIpv4Mask.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.num_err_32, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String ipv4Gateway = Objects.requireNonNull(tvIpv4Gateway.getText()).toString();
        if (!TextUtils.isEmpty(ipv4Gateway)) {
            if (!CheckUtils.ipCheck(ipv4Gateway)) {
                tvIpv4Gateway.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String ipv6Address = Objects.requireNonNull(tvIpv6Address.getText()).toString();
        if (!TextUtils.isEmpty(ipv6Address)) {
            if (!CheckUtils.ip6Check(ipv6Address)) {
                tvIpv6Address.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String prefix = Objects.requireNonNull(tvIpv6Prefix.getText()).toString();
        if (!TextUtils.isEmpty(prefix)) {
            int ipv6Prefix = Integer.parseInt(prefix);
            if (ipv6Prefix < 1 || ipv6Prefix > 128) {
                tvIpv6Prefix.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.num_err_128, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String ipv6Gateway = Objects.requireNonNull(tvIpv6Gateway.getText()).toString();
        if (!TextUtils.isEmpty(ipv6Gateway)) {
            if (!CheckUtils.ip6Check(ipv6Gateway)) {
                tvIpv6Gateway.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String ratedPower = Objects.requireNonNull(tvRatedPowe.getText()).toString();
        if (!TextUtils.isEmpty(ratedPower)) {
            int ratedPowe = Integer.parseInt(ratedPower);
            if (ratedPowe < 1 || ratedPowe > 100000) {
                tvRatedPowe.setTextColor(Color.RED);
                ToastUtil.customToastView(mContext, R.string.num_err_10w, Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                return false;
            }
        }

        String message = Objects.requireNonNull(tvMessage.getText()).toString();
        if (!CheckUtils.stringCheck(message)) {
            tvMessage.setTextColor(Color.RED);
            ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }

        return true;
    }

    @SuppressLint("InflateParams")
    public void writeTag(Tag tag) {
        ToastUtil.customToastView(mContext, getString(R.string.write_ing), Toast.LENGTH_SHORT
                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            int index = 5;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("51", Objects.requireNonNull(tvUserName.getText()).toString().trim());
            jsonObject.put("52", Objects.requireNonNull(tvPwd.getText()).toString().trim());
            jsonObject.put("53", Objects.requireNonNull(tvDataCenter.getText()).toString().trim());
            jsonObject.put("54", Objects.requireNonNull(tvDataCenterName.getText()).toString().trim());
            jsonObject.put("55", Objects.requireNonNull(tvRow.getText()).toString().trim());
            jsonObject.put("56", Objects.requireNonNull(tvColumn.getText()).toString().trim());
            jsonObject.put("57", Objects.requireNonNull(tvIpv4Address.getText()).toString().trim());
            jsonObject.put("58", Objects.requireNonNull(tvIpv4Mask.getText()).toString().trim());
            jsonObject.put("59", Objects.requireNonNull(tvIpv4Gateway.getText()).toString().trim());
            jsonObject.put("60", Objects.requireNonNull(tvIpv6Address.getText()).toString().trim());
            jsonObject.put("61", Objects.requireNonNull(tvIpv6Prefix.getText()).toString().trim());
            jsonObject.put("62", Objects.requireNonNull(tvIpv6Gateway.getText()).toString().trim());
            jsonObject.put("63", Objects.requireNonNull(tvRatedPowe.getText()).toString().trim());
            jsonObject.put("64", spinnerType.getSelectedItemId());
            jsonObject.put("65", Objects.requireNonNull(tvMessage.getText()).toString().trim());
            if (rbRead.isChecked()) {
                jsonObject.put("66", "1");
            } else {
                jsonObject.put("66", "2");
            }


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
                Log.e("ZM", "crc: " + crc);
                byte[] writelens = {(byte) 0x00, (byte) (jsonObject.toString().length())};
                byte[] concatAll = DataUtils.concatAll(writelens, DataUtils.hexStringToByteArray(crc));
                Log.e("ZM", "Page4: " + DataUtils.toHexString(concatAll));
                ultralight.writePage(4, concatAll);
            }

            ultralight.close();
            boolean shake = MySharedPreferences.getBoolean("shake", true);
            if (shake) {
                mContext.vibrate(mContext, 500);
            }
            boolean sound = MySharedPreferences.getBoolean("sound", true);
            if (sound) {
                mContext.setPlaySound(true);
            }
            ToastUtil.customToastView(mContext, getString(R.string.write_success), Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ZM", "Exception: " + e.toString());
            ToastUtil.customToastView(mContext, getString(R.string.write_failed), Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            boolean shake = MySharedPreferences.getBoolean("shake", true);
            if (shake) {
                mContext.vibrate(mContext, 1000);
            }
            boolean sound = MySharedPreferences.getBoolean("sound", true);
            if (sound) {
                mContext.setPlaySound(false);
            }
        } finally {
            try {
                ultralight.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean tvusernameFlag = false;
    private boolean tvpwdFlag = false;
    private boolean tvdatacenterFlag = false;
    private boolean tvDataCenterNameFlag = false;
    private boolean tvRowFlag = false;
    private boolean tvColumnFlag = false;

    /**
     * 当必填项不为空时，写入按钮才可以被点击
     */
    public void setBtnEnable() {
        if (rbRead.isChecked()) {
            if (tvusernameFlag && tvpwdFlag) {
                btnWrite.setEnabled(true);
            } else {
                btnWrite.setEnabled(false);
            }
        } else {
            if (tvusernameFlag && tvpwdFlag && tvdatacenterFlag && tvDataCenterNameFlag && tvRowFlag && tvColumnFlag) {
                btnWrite.setEnabled(true);
            } else {
                btnWrite.setEnabled(false);
            }
        }
    }

    private void textWatcher() {
        tvUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                tvusernameFlag = !TextUtils.isEmpty(words);
                setBtnEnable();
                boolean check = CheckUtils.stringCheck(words);
                if (!check) {
                    // 当输入非法字符时，将字体颜色设置为红色，并给出提示
                    tvUserName.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    // 当删除非法字符时，将字体颜色重新设置为黑色
                    tvUserName.setTextColor(Color.BLACK);
                }
            }
        });
        tvPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                tvpwdFlag = !TextUtils.isEmpty(words);
                setBtnEnable();
            }
        });
        tvDataCenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                tvdatacenterFlag = !TextUtils.isEmpty(words);
                setBtnEnable();
                boolean check = CheckUtils.stringCheck(words);
                if (!check) {
                    tvDataCenter.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    // 当删除非法字符时，将字体颜色重新设置为黑色
                    tvDataCenter.setTextColor(Color.BLACK);
                }
            }
        });
        tvDataCenterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                tvDataCenterNameFlag = !TextUtils.isEmpty(words);
                setBtnEnable();
                boolean check = CheckUtils.stringCheck(words);
                if (!check) {
                    tvDataCenterName.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    // 当删除非法字符时，将字体颜色重新设置为黑色
                    tvDataCenterName.setTextColor(Color.BLACK);
                }
            }
        });
        tvMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                boolean check = CheckUtils.stringCheck(words);
                if (!check) {
                    tvMessage.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    // 当删除非法字符时，将字体颜色重新设置为黑色
                    tvMessage.setTextColor(Color.BLACK);
                }
            }
        });
        tvRow.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    tvRowFlag = true;
                    double aDouble = Double.parseDouble(s.toString());
                    if (aDouble <= 0 || aDouble > 500) {
                        tvRow.setText("");
                        ToastUtil.customToastView(mContext, R.string.num_err_500, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                } else {
                    tvRowFlag = false;
                }
                setBtnEnable();
            }
        });

        tvColumn.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    tvColumnFlag = true;
                    double aDouble = Double.parseDouble(s.toString());
                    if (aDouble <= 0 || aDouble > 500) {
                        tvColumn.setText("");
                        ToastUtil.customToastView(mContext, R.string.num_err_500, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                } else {
                    tvColumnFlag = false;
                }
                setBtnEnable();
            }
        });

        tvIpv4Mask.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    double aDouble = Double.parseDouble(s.toString());
                    if (aDouble <= 0 || aDouble > 32) {
                        tvIpv4Mask.setText("");
                        ToastUtil.customToastView(mContext, R.string.num_err_32, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                }

            }
        });
        tvIpv6Prefix.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                //这里的处理是不让输入0开头的值
                String words = s.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    double aDouble = Double.parseDouble(s.toString());
                    if (aDouble <= 0 || aDouble > 128) {
                        tvIpv6Prefix.setText("");
                        ToastUtil.customToastView(mContext, R.string.num_err_128, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                }

            }
        });

        tvRatedPowe.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("InflateParams")
            @Override
            public void afterTextChanged(Editable s) {
                //这里的处理是不让输入0开头的值
                String words = s.toString();
                //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    double aDouble = Double.parseDouble(s.toString());
                    if (aDouble <= 0 || aDouble > 100000) {
                        tvRatedPowe.setText("");
                        ToastUtil.customToastView(mContext, R.string.num_err_10w, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                }

            }
        });
        //IPv4地址
        tvIpv4Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                if (!TextUtils.isEmpty(words)) {
                    if (!CheckUtils.ipCheck(words)) {
                        tvIpv4Address.setTextColor(Color.RED);
                    } else {
                        tvIpv4Address.setTextColor(Color.BLACK);
                    }
                }
            }
        });
        tvIpv4Gateway.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                if (!TextUtils.isEmpty(words)) {
                    if (!CheckUtils.ipCheck(words)) {
                        tvIpv4Gateway.setTextColor(Color.RED);
                    } else {
                        tvIpv4Gateway.setTextColor(Color.BLACK);
                    }
                }
            }
        });
        tvIpv6Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                if (!TextUtils.isEmpty(words)) {
                    if (!CheckUtils.ip6Check(words)) {
                        tvIpv6Address.setTextColor(Color.RED);
                    } else {
                        tvIpv6Address.setTextColor(Color.BLACK);
                    }
                }
            }
        });
        tvIpv6Gateway.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String words = s.toString();
                if (!TextUtils.isEmpty(words)) {
                    if (!CheckUtils.ip6Check(words)) {
                        tvIpv6Gateway.setTextColor(Color.RED);
                    } else {
                        tvIpv6Gateway.setTextColor(Color.BLACK);
                    }
                }
            }
        });

    }


    private void getFocusable(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
