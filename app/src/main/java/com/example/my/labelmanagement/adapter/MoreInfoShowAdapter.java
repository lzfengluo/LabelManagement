package com.example.my.labelmanagement.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.my.labelmanagement.AgainWriteActivity;
import com.example.my.labelmanagement.MoreInfoShowActivity;
import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.MoreInfoShowBean;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;
import com.example.my.labelmanagement.been.RvMoreAdapterBeanDao;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.dialog.AgainWriteDialog;
import com.example.my.labelmanagement.utils.Crc16Utils;
import com.example.my.labelmanagement.utils.DataUtils;
import com.example.my.labelmanagement.utils.Math;
import com.example.my.labelmanagement.utils.MySharedPreferences;
import com.example.my.labelmanagement.utils.ToastUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


/**
 * @author 张智超
 * @date 2019/4/16
 */
public class MoreInfoShowAdapter extends BaseSectionQuickAdapter<MoreInfoShowBean, BaseViewHolder> {
    private MoreInfoShowActivity mContext;
    private Tag tag;

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public MoreInfoShowAdapter(int layoutResId, int sectionHeadResId, List<MoreInfoShowBean> data, MoreInfoShowActivity mContext) {
        super(layoutResId, sectionHeadResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MoreInfoShowBean item) {
        final TagInfoBean tagInfoBean = item.getTagInfoBean();
        Log.d("zzc:", "convert: " + tagInfoBean.toString());
        helper.setText(R.id.tv_sn, tagInfoBean.getSN());
        helper.setText(R.id.tv_number, item.getNumber() + "");
        TextView view = helper.getView(R.id.tv_status);
        if (tagInfoBean.getIsWrite()) {
            view.setText(mContext.getResources().getString(R.string.writed));
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.yuan_sure);
            drawable.setBounds(0, 0, 32, 32);
            view.setCompoundDrawables(drawable, null, null, null);
        } else {
            view.setText(mContext.getResources().getString(R.string.no_write));
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.yuan_cancel);
            drawable.setBounds(0, 0, 32, 32);
            view.setCompoundDrawables(drawable, null, null, null);
        }

        ConstraintLayout constraintLayout = helper.getView(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AgainWriteActivity.class);
                intent.putExtra("SN", tagInfoBean.getSN());
                intent.putExtra("Bean", tagInfoBean);
                mContext.startActivity(intent);
            }
        });

        Button btn = helper.getView(R.id.btn_import);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagInfoBean.getIsWrite()) {
                    AgainWriteDialog againWriteDialog = new AgainWriteDialog(mContext, tagInfoBean);
                    againWriteDialog.setOnSureListener(new AgainWriteDialog.OnSureListener() {
                        @Override
                        public void click() {
                            if (null == tag) {
                                mContext.setPlaySound(false);
                                ToastUtil.customToastView(mContext, mContext.getString(R.string.near), Toast.LENGTH_SHORT
                                        , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                            } else {
                                helper.getView(R.id.btn_import).setEnabled(false);
                                writeTag(tag, tagInfoBean);
                            }
                        }
                    });
                    againWriteDialog.show();
                } else {
                    if (null == tag) {
                        mContext.setPlaySound(false);
                        ToastUtil.customToastView(mContext, mContext.getString(R.string.near), Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                    } else {
                        helper.getView(R.id.btn_import).setEnabled(false);
                        writeTag(tag, tagInfoBean);
                    }
                }
            }
        });
    }

    @Override
    protected void convertHead(BaseViewHolder helper, MoreInfoShowBean item) {
        helper.setText(R.id.tv_header, item.header);
    }


    private JSONObject jsonObject;
    private int index = 5;

    public void writeTag(Tag tag, TagInfoBean tagInfoBean) {
        ToastUtil.customToastView(mContext, mContext.getString(R.string.write_ing), Toast.LENGTH_SHORT
                , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            index = 5;
            jsonObject = new JSONObject();
            jsonObject.put("1", tagInfoBean.getModel());
            jsonObject.put("2", tagInfoBean.getType());
            jsonObject.put("3", tagInfoBean.getVendor());
            jsonObject.put("4", tagInfoBean.getSN());
            jsonObject.put("5", tagInfoBean.getPartNum());
            jsonObject.put("6", tagInfoBean.getOccupiedHeight());
            jsonObject.put("7", tagInfoBean.getLifecycle());
            jsonObject.put("8", tagInfoBean.getFirstUse());
            jsonObject.put("9", tagInfoBean.getWeight());
            jsonObject.put("10", tagInfoBean.getRatedPower());
            jsonObject.put("11", tagInfoBean.getOwner());


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
            String tmp = "";
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
                mContext.vibrate(mContext, 500);
            }
            boolean sound = MySharedPreferences.getBoolean("sound", true);
            if (sound) {
                mContext.setPlaySound(true);
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
            ToastUtil.customToastView(mContext, mContext.getString(R.string.write_success), Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ZM", "Exception: " + e.toString());
            ToastUtil.customToastView(mContext, mContext.getString(R.string.write_failed), Toast.LENGTH_SHORT
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
}
