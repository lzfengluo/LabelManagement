package com.example.my.labelmanagement.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.been.HistoryBean;

import java.util.List;

/**
 * @author 张智超
 * @date 2019/4/14
 */
public class HistoryAdapter extends BaseQuickAdapter<HistoryBean, BaseViewHolder> {
    public HistoryAdapter(int layoutResId, @Nullable List<HistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryBean item) {
        helper.setText(R.id.tv_file_name, item.getFileName());
        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_add_num, item.getAddNum()+"");
        helper.setText(R.id.tv_update_num, item.getUpDataNum()+"");
        helper.setText(R.id.tv_del_num, item.getDelNum()+"");
    }
}
