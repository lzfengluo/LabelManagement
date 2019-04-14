package com.example.my.labelmanagement.adapter;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.my.labelmanagement.been.FeatureBean;
import com.example.my.labelmanagement.been.FeatureImportBean;
import com.example.my.labelmanagement.utils.FileUtils;
import com.example.my.labelmanagement.utils.xls.XssfExcelHelper;

import java.io.File;
import java.util.List;



public class MyAsyncListUtil extends AsyncListUtil<FeatureBean> {

    /**
     * 一次加载数据的个数，分页数量
     */
    private static final int TILE_SIZE = 4;

    public MyAsyncListUtil(RecyclerView mRecyclerView) {
        super(FeatureBean.class, TILE_SIZE, new MyDataCallback(), new MyViewCallback(mRecyclerView));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 更新当前可见范围数据
                onRangeChanged();
            }
        });
    }

    /**
     * 获取数据回调
     */
    public static class MyDataCallback extends DataCallback<FeatureBean> {
        List<File> suffixFile = null;

        /**
         * 总数据个数
         */
        @Override
        public int refreshData() {
            try {
                File[] files = FileUtils.getFiles(Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/feature");
                suffixFile = FileUtils.getSuffixFile(files, ".xlsx");
                return suffixFile.size();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }

        }

        /**
         * 填充数据（后台线程），一般为读取数据库数据
         */
        @Override
        public void fillData(@NonNull FeatureBean[] data, int startPosition, int itemCount) {
            if (suffixFile != null) {
                for (int i = 0; i < itemCount; i++) {
                    FeatureBean item = data[i];
                    if (item == null) {
                        item = new FeatureBean();
                        File file = suffixFile.get(i);
                        XssfExcelHelper helper = XssfExcelHelper.getInstance(file);
                        List<FeatureImportBean> infoBeanList = null;
                        try {
                            infoBeanList = helper.readExcel(FeatureImportBean.class, new String[]{"OperationType",
                                    "SN", "Model", "Type", "Vendor", "OccupiedHeight", "Lifecycle", "Weight",
                                    "RatedPower", "Owner", "PartNum"}, 0, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (infoBeanList.size() != 0) {
                            item.setName(file.getName());
                            item.setNum(infoBeanList.size());
                            item.setImport(false);
                            item.setLists(infoBeanList);
                        }
                        data[i] = item;
                    }
                }
            }
        }
    }

    /**
     * 用于获取可见项范围和更新通知的回调
     */
    public static class MyViewCallback extends ViewCallback {

        private RecyclerView mRecyclerView;

        public MyViewCallback(RecyclerView mRecyclerView) {
            this.mRecyclerView = mRecyclerView;
        }

        /**
         * 展示数据的范围
         */
        @Override
        public void getItemRangeInto(@NonNull int[] outRange) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            LinearLayoutManager mgr = (LinearLayoutManager) manager;
            outRange[0] = mgr.findFirstVisibleItemPosition();
            outRange[1] = mgr.findLastVisibleItemPosition();
        }

        /**
         * 刷新数据
         */
        @Override
        public void onDataRefresh() {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }

        /**
         * Item更新
         */
        @Override
        public void onItemLoaded(int position) {
            mRecyclerView.getAdapter().notifyItemChanged(position);
        }
    }
}
