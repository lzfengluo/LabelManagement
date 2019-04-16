package com.example.my.labelmanagement.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.FeatureBean;
import com.example.my.labelmanagement.been.FeatureImportBean;
import com.example.my.labelmanagement.been.HistoryBean;
import com.example.my.labelmanagement.dialog.ErrorDialog;
import com.example.my.labelmanagement.dialog.SuccessDialog;
import com.example.my.labelmanagement.utils.FileUtils;
import com.example.my.labelmanagement.utils.xls.DateUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * @author 张智超
 * @date 2019/4/14
 */
public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private MyAsyncListUtil mMyAsyncListUtil;
    private Context mContext;
    private ConstraintLayout mLlNull;

    public FeatureAdapter(Context mContext, MyAsyncListUtil mMyAsyncListUtil, ConstraintLayout mLlNull) {
        this.mMyAsyncListUtil = mMyAsyncListUtil;
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mLlNull = mLlNull;
    }

    @Override
    @NonNull
    public FeatureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_feature_import, parent, false));
    }

    @Override
    public int getItemCount() {
//        if (mMyAsyncListUtil.getItemCount() == 0) {
//            mLlNull.setVisibility(View.VISIBLE);
//        } else {
//            mLlNull.setVisibility(View.GONE);
//        }
        return mMyAsyncListUtil.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull final FeatureAdapter.ViewHolder holder, final int position) {
        final FeatureBean bean = mMyAsyncListUtil.getItem(position);
        // 有可能获取为空，这是可以显示加载中，等待同步数据。
        if (bean == null) {
            holder.mTvFileName.setText(R.string.data_load);
            holder.mBtnImport.setVisibility(View.GONE);
        } else {
            holder.mTvFileName.setText(bean.getName());
            holder.mTvNum.setText(bean.getNum() + "");
            holder.mBtnImport.setVisibility(View.VISIBLE);
            holder.mBtnImport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<FeatureImportBean> lists = bean.getLists();
                    loadFileToDb(lists, holder, bean.getName());
                }
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvFileName;
        TextView mTvNum;
        Button mBtnImport;
        TextView mTvImportSuccess;
        TextView mTvImportFailed;
        TextView mTvDel1;
        TextView mTvDel2;

        ViewHolder(View itemView) {
            super(itemView);
            mTvFileName = itemView.findViewById(R.id.tv_file_name);
            mTvNum = itemView.findViewById(R.id.tv_num);
            mBtnImport = itemView.findViewById(R.id.btn_import);
            mTvImportSuccess = itemView.findViewById(R.id.tv_import_success);
            mTvImportFailed = itemView.findViewById(R.id.tv_import_failed);
            mTvDel1 = itemView.findViewById(R.id.tv_del1);
            mTvDel2 = itemView.findViewById(R.id.tv_del2);
        }
    }

    /**
     * 导入数据类
     *
     * @param lists
     */
    private void loadFileToDb(final List<FeatureImportBean> lists, final FeatureAdapter.ViewHolder holder, final String name) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                try {
                    int add = 0;
                    int update = 0;
                    int del = 0;
                    for (FeatureImportBean list : lists) {
                        String operationType = list.getOperationType();
                        if ("Add".equals(operationType)) {
                            MyApp.getDaoInstant().getFeatureImportBeanDao().insert(list);
                            add++;
                        } else if ("Update".equals(operationType)) {
                            MyApp.getDaoInstant().getFeatureImportBeanDao().update(list);
                            update++;
                        } else if ("Delete".equals(operationType)) {
                            MyApp.getDaoInstant().getFeatureImportBeanDao().delete(list);
                            del++;
                        }

                    }
                    HistoryBean historyBean = new HistoryBean();
                    historyBean.setTime(DateUtils.getCurrentTimeMillis(DateUtils.FORMAT_YMD));
                    historyBean.setFileName(name);
                    historyBean.setAddNum(add);
                    historyBean.setUpDataNum(update);
                    historyBean.setDelNum(del);
                    MyApp.getDaoInstant().getHistoryBeanDao().insertOrReplace(historyBean);

                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }

            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.disposable = d;
                    }

                    @Override
                    public void onNext(Integer value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        holder.mTvImportSuccess.setVisibility(View.GONE);
                        holder.mTvImportFailed.setVisibility(View.VISIBLE);
                        ErrorDialog errorDialog = new ErrorDialog(mContext, e.toString(), "/LabelRFID/feature/log/","");
                        errorDialog.show();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        holder.mTvImportSuccess.setVisibility(View.VISIBLE);
                        holder.mTvImportFailed.setVisibility(View.GONE);
                        holder.mBtnImport.setVisibility(View.GONE);

                        SuccessDialog successDialog = new SuccessDialog(mContext);
                        successDialog.setCancelable(false);
                        successDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                holder.mTvDel1.setVisibility(View.VISIBLE);
                                holder.mTvDel2.setVisibility(View.VISIBLE);
                                int time = 5;
                                CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        holder.mTvDel1.setText(millisUntilFinished / 1000 + "");
                                    }

                                    @Override
                                    public void onFinish() {
                                        holder.mTvDel1.setVisibility(View.GONE);
                                        holder.mTvDel2.setText(R.string.del_file);
                                        FileUtils.deleteFile(Environment.getExternalStorageDirectory()
                                                + File.separator + "LabelRFID/feature/" + name);
                                    }
                                }.start();

                            }
                        });
                        successDialog.show();
                        disposable.dispose();
                    }
                });
    }
}
