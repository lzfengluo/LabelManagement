package com.example.my.labelmanagement.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;
import com.example.my.labelmanagement.been.RvMoreAdapterBeanDao;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.been.TagInfoBeanDao;
import com.example.my.labelmanagement.dialog.ErrorDialog;
import com.example.my.labelmanagement.utils.CheckUtils;
import com.example.my.labelmanagement.utils.ToastUtil;

import java.util.List;

/**
 * @author 张智超
 * @date 2019/4/13
 */
public class TagAdapter extends BaseQuickAdapter<RvMoreAdapterBean, BaseViewHolder> {
    private Context mContext;

    public TagAdapter(Context mContext, int layoutResId, @Nullable List<RvMoreAdapterBean> data) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RvMoreAdapterBean item) {
        boolean isCheck = item.getIsCheck();
        if (isCheck) {
            boolean isCheckSuccess = item.getIsCheckSuccess();
            if (isCheckSuccess) {
                helper.setGone(R.id.tv1, false);
                helper.setGone(R.id.tv_num, false);
                helper.setGone(R.id.btn_import, false);
                helper.setGone(R.id.tv_write_num, true);
                helper.setGone(R.id.tv_all_num, true);
                helper.setGone(R.id.tv_write, true);
                helper.setGone(R.id.tv_all, true);
                helper.setGone(R.id.progressBar, true);
                helper.setGone(R.id.tv_check_success, true);
                helper.setGone(R.id.tv_check_failed, false);
                ProgressBar progressBar = helper.getView(R.id.progressBar);
                progressBar.setProgress(item.getWriteNum());
                progressBar.setMax(item.getAllNum());

            } else {
                helper.setGone(R.id.tv1, true);
                helper.setGone(R.id.tv_num, true);
                helper.setGone(R.id.btn_import, true);
                helper.setGone(R.id.tv_write_num, false);
                helper.setGone(R.id.tv_all_num, false);
                helper.setGone(R.id.tv_write, false);
                helper.setGone(R.id.tv_all, false);
                helper.setGone(R.id.progressBar, false);
                helper.setGone(R.id.tv_check_success, false);
                helper.setGone(R.id.tv_check_failed, true);
            }
        } else {
            //没校验过
            helper.setGone(R.id.tv1, true);
            helper.setGone(R.id.tv_num, true);
            helper.setGone(R.id.btn_import, true);
            helper.setGone(R.id.tv_write_num, false);
            helper.setGone(R.id.tv_all_num, false);
            helper.setGone(R.id.tv_write, false);
            helper.setGone(R.id.tv_all, false);
            helper.setGone(R.id.progressBar, false);
            helper.setGone(R.id.tv_check_success, false);
            helper.setGone(R.id.tv_check_failed, false);
        }

        helper.setText(R.id.tv_file_name, item.getName());
        helper.setText(R.id.tv_num, item.getAllNum() + "");
        helper.setText(R.id.tv_write_num, item.getWriteNum() + "");
        helper.setText(R.id.tv_all_num, item.getAllNum() + "");
        Button view = helper.getView(R.id.btn_import);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TagInfoBean> tagInfoBeanList = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.FileName.eq(item.getName())
                                , TagInfoBeanDao.Properties.IsCheck.eq(false)).list();
                String checkTag = CheckUtils.checkTag(tagInfoBeanList);

                RvMoreAdapterBean unique = MyApp.getDaoInstant().getRvMoreAdapterBeanDao().queryBuilder()
                        .where(RvMoreAdapterBeanDao.Properties.Name.eq(item.getName())).unique();
                if (TextUtils.isEmpty(checkTag)) {
                    unique.setIsCheck(true);
                    unique.setIsCheckSuccess(true);
                    unique.setWriteNum(0);
                    MyApp.getDaoInstant().getRvMoreAdapterBeanDao().update(unique);

                    helper.setGone(R.id.tv1, false);
                    helper.setGone(R.id.tv_num, false);
                    helper.setGone(R.id.btn_import, false);
                    helper.setGone(R.id.tv_write_num, true);
                    helper.setGone(R.id.tv_all_num, true);
                    helper.setGone(R.id.tv_write, true);
                    helper.setGone(R.id.tv_all, true);
                    helper.setGone(R.id.progressBar, true);
                    helper.setGone(R.id.tv_check_success, true);
                    helper.setGone(R.id.tv_check_failed, false);
                    ProgressBar progressBar = helper.getView(R.id.progressBar);
                    progressBar.setProgress(item.getWriteNum());
                    progressBar.setMax(item.getAllNum());
                    ToastUtil.customToastView(mContext, mContext.getString(R.string.jiaoyan_success), Toast.LENGTH_SHORT
                            , (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null));
                } else {
                    helper.setGone(R.id.tv_check_success, false);
                    helper.setGone(R.id.tv_check_failed, true);
                    unique.setIsCheck(true);
                    unique.setIsCheckSuccess(false);
                    MyApp.getDaoInstant().getRvMoreAdapterBeanDao().update(unique);

                    ErrorDialog errorDialog = new ErrorDialog(mContext, checkTag, "/LabelRFID/tag/log/"
                            , mContext.getString(R.string.check_failed));
                    errorDialog.show();
                }
            }
        });
    }

}
