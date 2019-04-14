package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.been.TagInfoBeanDao;
import com.example.my.labelmanagement.utils.FileUtils;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.example.my.labelmanagement.utils.xls.XssfExcelHelper;
import com.shizhefei.fragment.LazyFragment;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 文件管理界面
 * @author 张智超
 */
public class FileFragment extends LazyFragment implements View.OnClickListener {

    private android.widget.LinearLayout mLlLoading;
    private android.widget.ImageView mIvLoading;
    private android.widget.LinearLayout mLlFileFeature;
    private android.widget.LinearLayout mLlFileTag;
    private ImageView mIvFeature;
    private ImageView mIvTag;
    private Badge badgeTag;
    private Badge badgeFeature;
    private int featureSize = 0;
    private int tagSize = 0;

    public FileFragment() {
    }

    public FileFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_file);
        initView();
        initData();
    }

    private void initData() {
        badgeTag = new QBadgeView(getActivity()).bindTarget(mIvTag);
        badgeFeature = new QBadgeView(getActivity()).bindTarget(mIvFeature);
        mLlFileTag.setOnClickListener(this);
        mLlFileFeature.setOnClickListener(this);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();

    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        loadFileToDb();
        loadFeatureFile();
    }

    private void loadFeatureFile() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                File[] files = FileUtils.getFiles(Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/feature");
                if (files == null) {
                    e.onComplete();
                    return;
                }
                List<File> suffixFile = FileUtils.getSuffixFile(files, ".xlsx");
                if (suffixFile == null || suffixFile.size() == 0) {
                    e.onComplete();
                } else {
                    e.onNext(suffixFile.size());
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
                        badgeFeature.setBadgeNumber(value);
                        mLlFileFeature.setEnabled(true);
                        featureSize = value;
                        disposable.dispose();
                    }

                    @SuppressLint("InflateParams")
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.customToastView(getActivity(), getString(R.string.import_error), Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_toast, null));
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        mLlFileFeature.setEnabled(true);
                        featureSize = 0;
                        disposable.dispose();
                    }
                });
    }

    /**
     * 读取表格到数据库中
     */
    private void loadFileToDb() {
        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim);
        //解决旋转有停顿
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        mLlLoading.setVisibility(View.VISIBLE);
        mIvLoading.startAnimation(rotateAnimation);
        mLlFileFeature.setEnabled(false);
        mLlFileTag.setEnabled(false);
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                try {
                    File[] files = FileUtils.getFiles(Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/tag");
                    if (files == null) {
                        e.onComplete();
                        return;
                    }
                    List<File> suffixFile = FileUtils.getSuffixFile(files, ".xlsx");
                    if (suffixFile == null || suffixFile.size() == 0) {
                        e.onComplete();
                    } else {
                        List<RvMoreAdapterBean> rvMoreAdapterBeans = MyApp.getDaoInstant().getRvMoreAdapterBeanDao().loadAll();
                        for (File file : suffixFile) {
                            boolean isHave = false;
                            String fileName = file.getName();
                            for (RvMoreAdapterBean moreWriteFileName : rvMoreAdapterBeans) {
                                if (moreWriteFileName.getName().equals(fileName)) {
                                    isHave = true;
                                }
                            }
                            if (!isHave) {
                                XssfExcelHelper helper = XssfExcelHelper.getInstance(file);
                                List<TagInfoBean> infoBeanList = helper.readExcel(TagInfoBean.class, new String[]{"GroupInformation",
                                        "SN", "Model", "Type", "Vendor", "OccupiedHeight", "Lifecycle", "FirstUse", "Weight",
                                        "RatedPower", "Owner", "PartNum"}, 0, true);
                                if (infoBeanList.size() != 0) {
                                    for (TagInfoBean tagInfoBean : infoBeanList) {
                                        tagInfoBean.setFileName(fileName);
                                    }
                                    MyApp.getDaoInstant().getTagInfoBeanDao().insertOrReplaceInTx(infoBeanList);
                                    RvMoreAdapterBean rvMoreAdapterBean = new RvMoreAdapterBean();
                                    rvMoreAdapterBean.setName(fileName);
                                    List<TagInfoBean> tagInfoBeans = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                                            .where(TagInfoBeanDao.Properties.FileName.eq(fileName)).list();
                                    rvMoreAdapterBean.setAllNum(tagInfoBeans.size());
                                    MyApp.getDaoInstant().getRvMoreAdapterBeanDao().insertOrReplace(rvMoreAdapterBean);
                                }
                            }
                        }
                        List<RvMoreAdapterBean> loadAll = MyApp.getDaoInstant().getRvMoreAdapterBeanDao().loadAll();
                        e.onNext(loadAll.size());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        this.disposable = d;
                    }

                    @Override
                    public void onNext(Integer value) {
                        mLlLoading.setVisibility(View.GONE);
                        badgeTag.setBadgeNumber(value);
                        tagSize = value;
                        mLlFileTag.setEnabled(true);
                    }

                    @SuppressLint("InflateParams")
                    @Override
                    public void onError(Throwable e) {
                        mLlLoading.setVisibility(View.GONE);
                        ToastUtil.customToastView(getActivity(), R.string.import_error, Toast.LENGTH_SHORT
                                , (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_toast, null));
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        mLlLoading.setVisibility(View.GONE);
                        mLlFileTag.setEnabled(true);
                        tagSize = 0;
                        disposable.dispose();
                    }
                });
    }

    private void initView() {
        mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
        mIvLoading = (ImageView) findViewById(R.id.iv_loading);
        mLlFileFeature = (LinearLayout) findViewById(R.id.ll_file_feature);
        mLlFileTag = (LinearLayout) findViewById(R.id.ll_file_tag);
        mIvFeature = (ImageView) findViewById(R.id.iv_feature);
        mIvTag = (ImageView) findViewById(R.id.iv_tag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_file_feature:
                Intent intent = new Intent(getActivity(), FeatureActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_file_tag:
                Intent intent1 = new Intent(getActivity(), TagActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
