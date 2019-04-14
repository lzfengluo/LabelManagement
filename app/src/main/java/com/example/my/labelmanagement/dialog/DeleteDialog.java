package com.example.my.labelmanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.adapter.TagAdapter;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.been.TagInfoBeanDao;
import com.example.my.labelmanagement.utils.FileUtils;
import com.example.my.labelmanagement.utils.ToastUtil;

import java.util.List;

/**
 * @author 张智超
 * @date 2019/4/14
 */
public class DeleteDialog extends Dialog implements View.OnClickListener {
    private Button mBtnCancel;
    private Button mBtnSure;
    private List<RvMoreAdapterBean> data;
    private TagAdapter tagAdapter;
    private int position;
    private Context context;

    public DeleteDialog(@NonNull Context context, List<RvMoreAdapterBean> data, TagAdapter tagAdapter, int position) {
        super(context);
        this.data = data;
        this.tagAdapter = tagAdapter;
        this.position = position;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_delete);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sure:
                RvMoreAdapterBean rvMoreAdapterBean = data.get(position);
                MyApp.getDaoInstant().getRvMoreAdapterBeanDao().delete(rvMoreAdapterBean);
                List<TagInfoBean> tagInfoBeans = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.FileName.eq(rvMoreAdapterBean.getName())).list();
                MyApp.getDaoInstant().getTagInfoBeanDao().deleteInTx(tagInfoBeans);
                boolean deleteFile = FileUtils.deleteFile(Environment.getExternalStorageDirectory() + "/LabelRFID/tag/" + rvMoreAdapterBean.getName());
                if (deleteFile) {
                    data.remove(position);
                    tagAdapter.notifyItemRemoved(position);
                    dismiss();
                } else {
                    ToastUtil.customToastView(context, R.string.delete_failed, Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(context).inflate(R.layout.layout_toast, null));
                }

                break;
        }

    }

    private void initView() {
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        mBtnSure = (Button) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
    }
}
