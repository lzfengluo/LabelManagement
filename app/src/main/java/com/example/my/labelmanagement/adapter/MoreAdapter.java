package com.example.my.labelmanagement.adapter;

import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;

import java.util.List;

/**
 * @author 张智超
 * @date 2019/4/16
 */
public class MoreAdapter extends BaseQuickAdapter<RvMoreAdapterBean, BaseViewHolder> {
    public MoreAdapter(int layoutResId, @Nullable List<RvMoreAdapterBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RvMoreAdapterBean item) {
        helper.setText(R.id.tv_file_name, item.getName());
        helper.setText(R.id.tv_write_num, item.getWriteNum() + "");
        helper.setText(R.id.tv_all_num, item.getAllNum() + "");
        ProgressBar progressBar = helper.getView(R.id.progressBar);
        progressBar.setMax(item.getAllNum());
        progressBar.setProgress(item.getWriteNum());
    }
}
