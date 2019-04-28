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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.been.TagInfoBeanDao;
import com.example.my.labelmanagement.dialog.CustomDatePicker;
import com.example.my.labelmanagement.dialog.DateFormatUtils;
import com.example.my.labelmanagement.listener.OnNewIntentListener;
import com.example.my.labelmanagement.utils.CheckUtils;
import com.example.my.labelmanagement.utils.Crc16Utils;
import com.example.my.labelmanagement.utils.DataUtils;
import com.example.my.labelmanagement.utils.Math;
import com.example.my.labelmanagement.utils.MySharedPreferences;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.example.my.labelmanagement.view.ClearEditText;
import com.example.my.labelmanagement.view.MySpinner;

import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 单个写入 设备信息界面
 *
 * @author 张智超
 * @date 2019/4/13
 */
public class WriteSingleDeviceFragment extends LazyFragment implements OnNewIntentListener, View.OnClickListener {
    private WriteActivity mContext;
    private ClearEditText xl;
    private ImageView ivSearch;
    private ClearEditText xh;
    private TextView lb;
    private MySpinner spinnerType;
    private MySpinner spinnerZw;
    private MySpinner spinnerCs;
    private MySpinner spinnerXh;
    private ClearEditText zw;
    private ClearEditText cs;
    private ClearEditText zc;
    private TextView sj;
    private ImageView imageRili;
    private ClearEditText zl;
    private ClearEditText gl;
    private ClearEditText zcsyr;
    private ClearEditText bj;
    private Button btnWrite;
    private Tag tag;
    private CustomDatePicker mDatePicker;

    public WriteSingleDeviceFragment() {
    }

    public WriteSingleDeviceFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_write_single_device);
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

        imageRili.setOnClickListener(this);

        //获取焦点 光标出现
        xl.requestFocus();

        textWatcher();
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取到对象
                lb.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerZw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zw.setText(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cs.setText(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerXh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xh.setText(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initDatePicker();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (WriteActivity) context;
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

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
    }

    private void initView() {
        ivSearch = (ImageView) findViewById(R.id.search);
        ivSearch.setOnClickListener(this);
        xl = (ClearEditText) findViewById(R.id.xl);
        xh = (ClearEditText) findViewById(R.id.xh);
        lb = (TextView) findViewById(R.id.lb);
        spinnerType = (MySpinner) findViewById(R.id.spinner_type);
        zw = (ClearEditText) findViewById(R.id.zw);
        cs = (ClearEditText) findViewById(R.id.cs);
        zc = (ClearEditText) findViewById(R.id.zc);
        sj = (TextView) findViewById(R.id.sj);
        imageRili = (ImageView) findViewById(R.id.image_rili);
        zl = (ClearEditText) findViewById(R.id.zl);
        gl = (ClearEditText) findViewById(R.id.gl);
        zcsyr = (ClearEditText) findViewById(R.id.zcsyr);
        bj = (ClearEditText) findViewById(R.id.bj);
        btnWrite = (Button) findViewById(R.id.btn_write);
        setBtnEnable();
        LinearLayout mLlXl = (LinearLayout) findViewById(R.id.ll_xl);
        mLlXl.setOnClickListener(this);
        LinearLayout mLlXh = (LinearLayout) findViewById(R.id.ll_xh);
        mLlXh.setOnClickListener(this);
        LinearLayout mLlLb = (LinearLayout) findViewById(R.id.ll_lb);
        mLlLb.setOnClickListener(this);
        LinearLayout mLlZw = (LinearLayout) findViewById(R.id.ll_zw);
        mLlZw.setOnClickListener(this);
        LinearLayout mLlCs = (LinearLayout) findViewById(R.id.ll_cs);
        mLlCs.setOnClickListener(this);
        LinearLayout mLlZc = (LinearLayout) findViewById(R.id.ll_zc);
        mLlZc.setOnClickListener(this);
        LinearLayout mLlZl = (LinearLayout) findViewById(R.id.ll_zl);
        mLlZl.setOnClickListener(this);
        LinearLayout mLlGl = (LinearLayout) findViewById(R.id.ll_gl);
        mLlGl.setOnClickListener(this);
        LinearLayout mLlBj = (LinearLayout) findViewById(R.id.ll_bj);
        mLlBj.setOnClickListener(this);
        LinearLayout mLlZcsyr = (LinearLayout) findViewById(R.id.ll_zcsyr);
        mLlZcsyr.setOnClickListener(this);
        LinearLayout mLlSj = (LinearLayout) findViewById(R.id.ll_sj);
        mLlSj.setOnClickListener(this);
        spinnerZw = (MySpinner) findViewById(R.id.spinner_zw);
        spinnerCs = (MySpinner) findViewById(R.id.spinner_cs);
        spinnerXh = (MySpinner) findViewById(R.id.spinner_xh);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                String data = Objects.requireNonNull(xl.getText()).toString();
                getData(data);
                break;
            case R.id.image_rili:
            case R.id.ll_sj:
//                showDatePicker();
                mDatePicker.show(sj.getText().toString());
                break;
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
            case R.id.ll_lb:
                getFocusable(lb);
                break;
            case R.id.ll_xl:
                getFocusable(xl);
                break;
            case R.id.ll_xh:
                getFocusable(xh);
                break;
            case R.id.ll_zw:
                getFocusable(zw);
                break;
            case R.id.ll_cs:
                getFocusable(cs);
                break;
            case R.id.ll_zc:
                getFocusable(zc);
                break;
            case R.id.ll_zl:
                getFocusable(zl);
                break;
            case R.id.ll_gl:
                getFocusable(gl);
                break;
            case R.id.ll_bj:
                getFocusable(bj);
                break;
            case R.id.ll_zcsyr:
                getFocusable(zcsyr);
                break;
            default:
                break;

        }
    }

    private void getFocusable(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


    /**
     * 检查填写的项目合法性
     *
     * @return boolean
     */
    @SuppressLint("InflateParams")
    private boolean checkInput() {
        xh.setTextColor(Color.BLACK);
        lb.setTextColor(Color.BLACK);
        cs.setTextColor(Color.BLACK);
        bj.setTextColor(Color.BLACK);
        zcsyr.setTextColor(Color.BLACK);
        xl.setTextColor(Color.BLACK);
        boolean isFlag = true;
        if (!CheckUtils.stringCheck(Objects.requireNonNull(xh.getText()).toString())) {
            xh.setTextColor(Color.RED);
            isFlag = false;
        } else if (!CheckUtils.stringCheck(lb.getText().toString())) {
            lb.setTextColor(Color.RED);
            isFlag = false;
        } else if (!CheckUtils.stringCheck(Objects.requireNonNull(cs.getText()).toString())) {
            cs.setTextColor(Color.RED);
            isFlag = false;
        } else if (!CheckUtils.stringCheck(Objects.requireNonNull(zcsyr.getText()).toString())) {
            zcsyr.setTextColor(Color.RED);
            isFlag = false;
        } else if (!CheckUtils.stringCheck(Objects.requireNonNull(bj.getText()).toString())) {
            bj.setTextColor(Color.RED);
            isFlag = false;
        } else if (!CheckUtils.stringCheck(Objects.requireNonNull(xl.getText()).toString())) {
            xl.setTextColor(Color.RED);
            isFlag = false;
        }
        if (!isFlag) {
            ToastUtil.customToastView(mContext, R.string.write_string_err, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }

        if (xl.getText().toString().isEmpty() || lb.getText().toString().isEmpty()
                || Objects.requireNonNull(zw.getText()).toString().isEmpty()) {
            ToastUtil.customToastView(mContext, R.string.write_must, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
            return false;
        }
        return true;
    }


    JSONObject jsonObject;

    @SuppressLint("InflateParams")
    public void writeTag(Tag tag) {
        ToastUtil.customToastView(mContext, getString(R.string.write_ing), Toast.LENGTH_SHORT
                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            int index = 5;
            jsonObject = new JSONObject();
            jsonObject.put("1", Objects.requireNonNull(xh.getText()).toString().trim());
            jsonObject.put("2", lb.getText().toString().trim());
            jsonObject.put("3", Objects.requireNonNull(cs.getText()).toString().trim());
            jsonObject.put("4", Objects.requireNonNull(xl.getText()).toString().trim());
            jsonObject.put("5", Objects.requireNonNull(bj.getText()).toString().trim());
            jsonObject.put("6", Objects.requireNonNull(zw.getText()).toString().trim());
            jsonObject.put("7", Objects.requireNonNull(zc.getText()).toString().trim());
            jsonObject.put("8", sj.getText().toString().trim());
            jsonObject.put("9", Objects.requireNonNull(zl.getText()).toString().trim());
            jsonObject.put("10", Objects.requireNonNull(gl.getText()).toString().trim());
            jsonObject.put("11", Objects.requireNonNull(zcsyr.getText()).toString().trim());


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
                Log.e("ZM", "crc: " + crc + "---" + DataUtils.toHexString(jsonObject.toString().getBytes()));
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

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        sj.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(mContext, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                sj.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }


    @SuppressLint("InflateParams")
    public void getData(String data) {
        TagInfoBean unique = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder().where(TagInfoBeanDao.Properties.SN.eq(data)).unique();
        if (unique != null) {
            xl.setText(data);
            xh.setText(unique.getModel());
            lb.setText(unique.getType());
            zw.setText(unique.getOccupiedHeight());
            cs.setText(unique.getVendor());
            zc.setText(unique.getLifecycle());
            int round = java.lang.Math.round(Float.parseFloat(unique.getWeight()));
            zl.setText(String.valueOf(round));
            int edgl = java.lang.Math.round(Float.parseFloat(unique.getRatedPower()));
            gl.setText(String.valueOf(edgl));
            bj.setText(unique.getPartNum());
            zcsyr.setText(unique.getOwner());
        } else {
            ToastUtil.customToastView(mContext, getString(R.string.db_no_data), Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
        }
    }

    /**
     * 序列号标志
     */
    private boolean xlFlag = false;
    /**
     * 占用U位数
     */
    private boolean zwFlag = false;
    /**
     * 设备类型
     */
    private boolean lbFlag = false;

    /**
     * 当必填项不为空时，写入按钮才可以被点击
     */
    public void setBtnEnable() {
        if (xlFlag && zwFlag && lbFlag) {
            btnWrite.setEnabled(true);
        } else {
            btnWrite.setEnabled(false);
        }
    }

    private void textWatcher() {
        xl.addTextChangedListener(new TextWatcher() {
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
                xlFlag = !TextUtils.isEmpty(words);
                setBtnEnable();
                boolean check = CheckUtils.stringCheck(words);
                if (!check) {
                    xl.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    xl.setTextColor(Color.BLACK);
                }
            }
        });
        xh.addTextChangedListener(new TextWatcher() {
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
                    xh.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    xh.setTextColor(Color.BLACK);
                }
            }
        });
        cs.addTextChangedListener(new TextWatcher() {
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
                    cs.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    cs.setTextColor(Color.BLACK);
                }
                if ("Huawei".equals(words)) {
                    spinnerXh.setVisibility(View.VISIBLE);
                } else {
                    spinnerXh.setVisibility(View.GONE);
                    xh.setText("");
                }
            }
        });
        zcsyr.addTextChangedListener(new TextWatcher() {
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
                    zcsyr.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    zcsyr.setTextColor(Color.BLACK);
                }
            }
        });
        bj.addTextChangedListener(new TextWatcher() {
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
                    bj.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    bj.setTextColor(Color.BLACK);
                }
            }
        });
        lb.addTextChangedListener(new TextWatcher() {
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
                lbFlag = !TextUtils.isEmpty(words);
                setBtnEnable();
                boolean check = CheckUtils.stringCheck(words);
                if (!check) {
                    bj.setTextColor(Color.RED);
                    ToastUtil.customToastView(mContext, R.string.toast_legal, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    bj.setTextColor(Color.BLACK);
                }
            }
        });
        zw.addTextChangedListener(new TextWatcher() {

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
                    zwFlag = true;
                    double aDouble = Double.parseDouble(s.toString());
                    if (aDouble <= 0 || aDouble > 54) {
                        zw.setText("");
                        ToastUtil.customToastView(mContext, R.string.write_height_err, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                } else {
                    zwFlag = false;
                }
                setBtnEnable();
            }
        });

        zc.addTextChangedListener(new TextWatcher() {

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
                    if (aDouble <= 0 || aDouble > 20) {
                        zc.setText("");
                        ToastUtil.customToastView(mContext, R.string.write_shengmingzhouqi_err, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                }

            }
        });

        zl.addTextChangedListener(new TextWatcher() {

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
                    if (aDouble <= 0 || aDouble > 5000) {
                        zl.setText("");
                        ToastUtil.customToastView(mContext, R.string.write_weight_err, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                }

            }
        });
        gl.addTextChangedListener(new TextWatcher() {

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
                    if (aDouble <= 0 || aDouble > 10000) {
                        gl.setText("");
                        ToastUtil.customToastView(mContext, R.string.write_gonglv_err, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    }
                }

            }
        });
    }
}
