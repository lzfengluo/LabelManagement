package com.example.my.labelmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.my.labelmanagement.adapter.MoreAdapter;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.RvMoreAdapterBean;
import com.example.my.labelmanagement.been.RvMoreAdapterBeanDao;

import java.util.ArrayList;
import java.util.List;


/**
 * 批量写入所有校验过的xlsx展示界面
 *
 * @author 张智超
 * @date 2019/04/16
 */
public class WriteMoreActivity extends AppCompatActivity {

    private ImageView mIvMenu;
    private TextView mMToolbarTitle;
    private RecyclerView mRecyclerView;
    private List<RvMoreAdapterBean> list = new ArrayList<>();
    private MoreAdapter moreAdapter;
    private ConstraintLayout mLlNull;
    private TextView mTvFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_more);
        initView();
        initData();
    }


    private void initData() {
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteMoreActivity.this.finish();
            }
        });
        mMToolbarTitle.setText(R.string.write_more);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTvFile.setText(R.string.file_tag_address);

        moreAdapter = new MoreAdapter(R.layout.item_more, list);
        mRecyclerView.setAdapter(moreAdapter);
        moreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String name = list.get(position).getName();
                Intent intent = new Intent(WriteMoreActivity.this, MoreInfoShowActivity.class);
                intent.putExtra("FileName", name);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(MyApp.getDaoInstant().getRvMoreAdapterBeanDao().queryBuilder()
                .where(RvMoreAdapterBeanDao.Properties.IsCheckSuccess.eq(true)).list());
        if (list.size() == 0) {
            mLlNull.setVisibility(View.VISIBLE);
        } else {
            mLlNull.setVisibility(View.GONE);
        }
        moreAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mIvMenu = findViewById(R.id.iv_menu);
        mMToolbarTitle = findViewById(R.id.mToolbarTitle);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLlNull = findViewById(R.id.ll_null);
        mTvFile = findViewById(R.id.tv_file);
    }


}
