package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my.labelmanagement.adapter.TagAdapter;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;
import com.example.my.labelmanagement.been.RvMoreAdapterBeanDao;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.been.TagInfoBeanDao;
import com.example.my.labelmanagement.dialog.DeleteDialog;
import com.example.my.labelmanagement.dialog.ErrorDialog;
import com.example.my.labelmanagement.listener.OnItemClickListener;
import com.example.my.labelmanagement.utils.xls.XssfExcelHelper;
import com.example.my.labelmanagement.view.DeleteRecyclerView;
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
 * 设备标签清单表
 *
 * @author 张智超
 * @date 2019/4/14
 */
@SuppressLint("Registered")
public class TagActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIvMenu;
    private TextView mMToolbarTitle;
    private ImageView mIvMore;
    private DeleteRecyclerView mRecyclerView;
    private ConstraintLayout mLlNull;
    private TextView mTvFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        initView();
        initData();
    }

    private void initData() {
        mIvMore.setVisibility(View.VISIBLE);
        mIvMore.setOnClickListener(this);
        mIvMore.setImageResource(R.mipmap.icons_scanfile);
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagActivity.this.finish();
            }
        });
        mMToolbarTitle.setText(R.string.file_tag_title);
        mTvFile.setText(R.string.file_tag_address);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<RvMoreAdapterBean> data = MyApp.getDaoInstant().getRvMoreAdapterBeanDao().loadAll();
        if (data.size() == 0) {
            mLlNull.setVisibility(View.VISIBLE);
        } else {
            mLlNull.setVisibility(View.GONE);
        }
        final TagAdapter tagAdapter = new TagAdapter(this, R.layout.item_tag, data);
        mRecyclerView.setAdapter(tagAdapter);

        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                DeleteDialog deleteDialog = new DeleteDialog(TagActivity.this, data, tagAdapter, position);
                deleteDialog.show();
            }
        });
    }

    private void initView() {
        mIvMenu = findViewById(R.id.iv_menu);
        mMToolbarTitle = findViewById(R.id.mToolbarTitle);
        mIvMore = findViewById(R.id.iv_more);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLlNull = findViewById(R.id.ll_null);
        mTvFile = findViewById(R.id.tv_file);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_more:
                loadFileToDb();
                break;
            default:
                break;
        }
    }


    /**
     * 读取表格到数据库中
     */
    private KProgressHUD kProgressHUD;

    private void loadFileToDb() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        kProgressHUD.show();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) {
                try {
                    List<RvMoreAdapterBean> list = MyApp.getDaoInstant().getRvMoreAdapterBeanDao().queryBuilder()
                            .where(RvMoreAdapterBeanDao.Properties.IsCheck.eq(true), RvMoreAdapterBeanDao.Properties.IsCheckSuccess.eq(false)).list();
                    if (list.size() != 0) {
                        for (RvMoreAdapterBean rvMoreAdapterBean : list) {
                            MyApp.getDaoInstant().getRvMoreAdapterBeanDao().delete(rvMoreAdapterBean);
                            List<TagInfoBean> tagInfoBeanList = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                                    .where(TagInfoBeanDao.Properties.FileName.eq(rvMoreAdapterBean.getName())).list();
                            MyApp.getDaoInstant().getTagInfoBeanDao().deleteInTx(tagInfoBeanList);

                            String path = Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/tag/"
                                    + rvMoreAdapterBean.getName();
                            XssfExcelHelper helper = XssfExcelHelper.getInstance(new File(path));

                            List<TagInfoBean> infoBeanList = helper.readExcel(TagInfoBean.class, new String[]{"GroupInformation",
                                    "SN", "Model", "Type", "Vendor", "OccupiedHeight", "Lifecycle", "FirstUse", "Weight",
                                    "RatedPower", "Owner", "PartNum"}, 0, true);
                            if (infoBeanList.size() != 0) {
                                for (TagInfoBean tagInfoBean : infoBeanList) {
                                    tagInfoBean.setFileName(rvMoreAdapterBean.getName());
                                }
                                MyApp.getDaoInstant().getTagInfoBeanDao().insertOrReplaceInTx(infoBeanList);
                                RvMoreAdapterBean rvMoreAdapterBeanSave = new RvMoreAdapterBean();
                                rvMoreAdapterBeanSave.setName(rvMoreAdapterBean.getName());
                                List<TagInfoBean> tagInfoBeans = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                                        .where(TagInfoBeanDao.Properties.FileName.eq(rvMoreAdapterBean.getName())).list();
                                rvMoreAdapterBeanSave.setAllNum(tagInfoBeans.size());
                                MyApp.getDaoInstant().getRvMoreAdapterBeanDao().insertOrReplace(rvMoreAdapterBeanSave);
                            }
                        }
                    }
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
                        kProgressHUD.dismiss();
                        ErrorDialog errorDialog = new ErrorDialog(TagActivity.this, e.toString(), "/LabelRFID/tag/log/", "");
                        errorDialog.show();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        kProgressHUD.dismiss();
                        List<RvMoreAdapterBean> data = MyApp.getDaoInstant().getRvMoreAdapterBeanDao().loadAll();
                        if (data.size() == 0) {
                            mLlNull.setVisibility(View.VISIBLE);
                        } else {
                            mLlNull.setVisibility(View.GONE);
                        }
                        TagAdapter tagAdapter = new TagAdapter(TagActivity.this,
                                R.layout.item_tag, data);
                        mRecyclerView.swapAdapter(tagAdapter, false);
                        disposable.dispose();
                    }
                });
    }
}
