package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.labelmanagement.adapter.HistoryAdapter;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.HistoryBean;
import com.example.my.labelmanagement.dialog.ExportErrorDialog;
import com.example.my.labelmanagement.utils.DataUtils;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.example.my.labelmanagement.utils.xls.XssfExcelHelper;
import com.kaopiz.kprogresshud.KProgressHUD;

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
 * 历史记录界面
 *
 * @author 张智超
 * @date 2019/4/14
 */
public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIvMenu;
    private TextView mMToolbarTitle;
    private ImageView mIvMore;
    private RecyclerView mRecyclerView;
    private List<HistoryBean> historyBeanList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        initData();
    }

    private void initData() {
        mIvMore.setVisibility(View.VISIBLE);
        mIvMore.setOnClickListener(this);
        mIvMore.setImageResource(R.mipmap.icons_export);
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.this.finish();
            }
        });
        mMToolbarTitle.setText(R.string.history);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyBeanList = MyApp.getDaoInstant().getHistoryBeanDao().loadAll();
        HistoryAdapter historyAdapter = new HistoryAdapter(R.layout.item_history, historyBeanList);
        mRecyclerView.setAdapter(historyAdapter);
    }

    private void initView() {
        mIvMenu = findViewById(R.id.iv_menu);
        mMToolbarTitle = findViewById(R.id.mToolbarTitle);
        mIvMore = findViewById(R.id.iv_more);
        mRecyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_more:
                expect();
                break;
            default:
                break;
        }
    }

    private KProgressHUD kProgressHUD;

    private void expect() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        kProgressHUD.show();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) {
                try {
                    String path = Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/log/History.xlsx";
                    DataUtils.deleteFile(path);
                    XssfExcelHelper<HistoryBean> helper = XssfExcelHelper.getInstance(new File(path));
                    if (historyBeanList.size() != 0) {
                        helper.writeExcel(HistoryBean.class, historyBeanList, new String[]{"time", "fileName", "addNum", "upDataNum", "delNum"});
                        e.onNext(getString(R.string.expect_success));
                    } else {
                        e.onNext(getString(R.string.expect_nodata));
                    }

                } catch (Exception err) {
                    err.printStackTrace();
                    e.onError(err);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("InflateParams")
                    @Override
                    public void onNext(String value) {
                        ToastUtil.customToastView(HistoryActivity.this, value, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(HistoryActivity.this).inflate(R.layout.layout_toast, null));
                        kProgressHUD.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        kProgressHUD.dismiss();
                        ExportErrorDialog errorDialog = new ExportErrorDialog(HistoryActivity.this, e.toString(), "/LabelRFID/feature/log/");
                        errorDialog.show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
