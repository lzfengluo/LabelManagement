package com.example.my.labelmanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.been.TagInfoBean;


/**
 * @author 张智超
 * @date 2019/4/16
 */
public class AgainWriteDialog extends Dialog implements View.OnClickListener {
    private Button mBtnCancel;
    private Button mBtnSure;
    private TagInfoBean tagInfoBean;
    private Context mContext;

    public AgainWriteDialog(@NonNull Context context, TagInfoBean tagInfoBean) {
        super(context);
        this.tagInfoBean = tagInfoBean;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_again_write);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sure:
                dismiss();
                callBack();
                break;
            default:
                break;
        }

    }

    private OnSureListener onSureListener;

    public interface OnSureListener {
        void click();
    }

    public void setOnSureListener(OnSureListener onSureListener) {
        this.onSureListener = onSureListener;
    }

    private void callBack() {
        if (onSureListener != null) {
            onSureListener.click();
        }
    }

    private void initView() {
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        mBtnSure = (Button) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
    }
}
