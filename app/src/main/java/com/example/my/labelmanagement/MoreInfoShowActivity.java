package com.example.my.labelmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.labelmanagement.adapter.ListDropDownAdapter;
import com.example.my.labelmanagement.adapter.MoreInfoShowAdapter;
import com.example.my.labelmanagement.app.BaseNfcActivity;
import com.example.my.labelmanagement.app.MyApp;
import com.example.my.labelmanagement.been.MoreInfoShowBean;
import com.example.my.labelmanagement.been.TagInfoBean;
import com.example.my.labelmanagement.been.TagInfoBeanDao;
import com.example.my.labelmanagement.utils.ToastUtil;
import com.example.my.labelmanagement.view.DropDownMenu;

import java.util.ArrayList;
import java.util.List;


/**
 * 批量写入的详细xlsx信息展示界面
 *
 * @author 张智超
 * @date 2019/04/16
 */
public class MoreInfoShowActivity extends BaseNfcActivity {
    private ImageView mIvMenu;
    private TextView mMToolbarTitle;
    private DropDownMenu mDropDownMenu;
    private List<String> headers = new ArrayList<>();
    private List<View> popupViews = new ArrayList<>();
    private List<String> status = new ArrayList<>();
    private List<MoreInfoShowBean> tagInfoBeanArrayList = new ArrayList<>();
    private List<String> groupInformation;
    private MoreInfoShowAdapter moreInfoShowAdapter;
    private String classString = "";
    private String statusString = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_show);
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList(classString, statusString);
    }

    private void initData() {
        classString = getResources().getString(R.string.header_class);
        statusString = getResources().getString(R.string.header_status);
        String fileName = getIntent().getStringExtra("FileName");
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreInfoShowActivity.this.finish();
            }
        });
        mMToolbarTitle.setText(fileName);

        headers.add(getResources().getString(R.string.header_class));
        headers.add(getResources().getString(R.string.header_status));
        status.add(getResources().getString(R.string.header_status));
        status.add(getResources().getString(R.string.writed));
        status.add(getResources().getString(R.string.no_write));
        ListView classListView = new ListView(this);
        classListView.setDividerHeight(0);
        classListView.setPadding(0, 0, 0, 16);
        List<TagInfoBean> tagInfoBeans = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                .where(TagInfoBeanDao.Properties.FileName.eq(fileName)).list();
        groupInformation = new ArrayList<>();
        groupInformation.add(getResources().getString(R.string.header_class));
        for (TagInfoBean tagInfoBean : tagInfoBeans) {
            String information = tagInfoBean.getGroupInformation();
            if (TextUtils.isEmpty(information)) {
                information = getString(R.string.ungrouped);
            }
            if (!groupInformation.contains(information)) {
                groupInformation.add(information);
            }
        }
        final ListDropDownAdapter classAdapter = new ListDropDownAdapter(this, groupInformation);
        classListView.setAdapter(classAdapter);

        ListView statusListView = new ListView(this);
        statusListView.setDividerHeight(0);
        statusListView.setPadding(0, 0, 0, 16);
        statusListView.setBackground(getDrawable(R.drawable.bg_white_bottom_8dp));
        final ListDropDownAdapter statusAdapter = new ListDropDownAdapter(this, status);
        statusListView.setAdapter(statusAdapter);

        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classAdapter.setCheckItem(position);
                String select = groupInformation.get(position);
                updateList(select, statusString);
                mDropDownMenu.setTabText(select);
                classString = select;
                mDropDownMenu.closeMenu();
            }
        });

        statusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                statusAdapter.setCheckItem(position);
                String select = status.get(position);
                updateList(classString, select);
                mDropDownMenu.setTabText(select);
                statusString = select;
                mDropDownMenu.closeMenu();
            }
        });

        popupViews.add(classListView);
        popupViews.add(statusListView);

        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(this).inflate(R.layout.item_drop_content, null);

        RecyclerView mRecyclerView = inflate.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setBackground(getDrawable(R.drawable.bg_white_bottom_8dp));
//        loadAllData();
        moreInfoShowAdapter = new MoreInfoShowAdapter(R.layout.item_group_content, R.layout.item_header, tagInfoBeanArrayList, this);
        mRecyclerView.setAdapter(moreInfoShowAdapter);
        mDropDownMenu.setDropDownMenu(headers, popupViews, inflate);
        handler.postDelayed(runnable, 0);
    }

    @Override
    protected void onDestroy() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.contains("MifareUltralight")) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            ToastUtil.customToastView(getApplicationContext(), R.string.support, Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_toast, null));
            return;
        }

        moreInfoShowAdapter.setTag(tag);
    }


    @SuppressLint("InflateParams")
    private void updateList(String classString, String statusString) {
        tagInfoBeanArrayList.clear();
        if (classString.equals(getResources().getString(R.string.header_class)) && statusString.equals(getResources().getString(R.string.header_status))) {
            loadAllData();
            moreInfoShowAdapter.notifyDataSetChanged();
        } else if (classString.equals(getResources().getString(R.string.header_class))) {
            boolean isWrite = false;
            boolean all = true;
            if (getResources().getString(R.string.writed).equals(statusString)) {
                isWrite = true;
                all = false;
            } else if (getResources().getString(R.string.no_write).equals(statusString)) {
                all = false;
            }
            for (String group : groupInformation) {
                List<TagInfoBean> list;
                if (getString(R.string.ungrouped).equals(group)) {
                    group = "";
                }
                if (all) {
                    list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                            .where(TagInfoBeanDao.Properties.GroupInformation.eq(group)).list();
                } else {
                    list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                            .where(TagInfoBeanDao.Properties.GroupInformation.eq(group), TagInfoBeanDao.Properties.IsWrite.eq(isWrite)).list();
                }

                if (list.size() != 0) {
                    if (TextUtils.isEmpty(group)) {
                        group = getString(R.string.ungrouped);
                    }
                    MoreInfoShowBean moreInfoShowBean = new MoreInfoShowBean(true, group);
                    tagInfoBeanArrayList.add(moreInfoShowBean);
                    for (int i = 0; i < list.size(); i++) {
                        tagInfoBeanArrayList.add(new MoreInfoShowBean(false, group, list.get(i), i + 1));
                    }

                }
            }
            if (tagInfoBeanArrayList.size() == 0) {
                ToastUtil.customToastView(getApplicationContext(), getString(R.string.no_select_data), Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_toast, null));
            }
            moreInfoShowAdapter.notifyDataSetChanged();
        } else if (statusString.equals(getResources().getString(R.string.header_status))) {
            List<TagInfoBean> list;
            if (getString(R.string.ungrouped).equals(classString)) {
                list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.GroupInformation.eq("")).list();
            } else {
                list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.GroupInformation.eq(classString)).list();
            }
            if (list.size() != 0) {
                MoreInfoShowBean moreInfoShowBean = new MoreInfoShowBean(true, classString);
                tagInfoBeanArrayList.add(moreInfoShowBean);
                for (int i = 0; i < list.size(); i++) {
                    tagInfoBeanArrayList.add(new MoreInfoShowBean(false, classString, list.get(i), i + 1));
                }
            } else {
                ToastUtil.customToastView(getApplicationContext(), getString(R.string.no_select_data), Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_toast, null));
            }
            moreInfoShowAdapter.notifyDataSetChanged();
        } else {
            boolean isWrite = false;
            if (getResources().getString(R.string.writed).equals(statusString)) {
                isWrite = true;
            }
            List<TagInfoBean> list;
            if (getString(R.string.ungrouped).equals(classString)) {
                list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.GroupInformation.eq(""), TagInfoBeanDao.Properties.IsWrite.eq(isWrite)).list();
            } else {
                list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.GroupInformation.eq(classString), TagInfoBeanDao.Properties.IsWrite.eq(isWrite)).list();
            }

            if (list.size() != 0) {
                MoreInfoShowBean moreInfoShowBean = new MoreInfoShowBean(true, classString);
                tagInfoBeanArrayList.add(moreInfoShowBean);
                for (int i = 0; i < list.size(); i++) {
                    tagInfoBeanArrayList.add(new MoreInfoShowBean(false, classString, list.get(i), i + 1));
                }
            } else {
                ToastUtil.customToastView(getApplicationContext(), getString(R.string.no_select_data), Toast.LENGTH_SHORT
                        , (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_toast, null));
            }
            moreInfoShowAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 查询所有分组下的数据
     */
    @SuppressLint("InflateParams")
    private void loadAllData() {
        for (String group : groupInformation) {
            List<TagInfoBean> list;
            if (getString(R.string.ungrouped).equals(group)) {
                list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.GroupInformation.eq("")).list();
            } else {
                list = MyApp.getDaoInstant().getTagInfoBeanDao().queryBuilder()
                        .where(TagInfoBeanDao.Properties.GroupInformation.eq(group)).list();
            }
            if (list.size() != 0) {
                MoreInfoShowBean moreInfoShowBean = new MoreInfoShowBean(true, group);
                tagInfoBeanArrayList.add(moreInfoShowBean);
                for (int i = 0; i < list.size(); i++) {
                    tagInfoBeanArrayList.add(new MoreInfoShowBean(false, group, list.get(i), i + 1));
                }

            }

        }
        if (tagInfoBeanArrayList.size() == 0) {
            ToastUtil.customToastView(getApplicationContext(), getString(R.string.no_select_data), Toast.LENGTH_SHORT
                    , (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_toast, null));
        }
    }

    private void initView() {
        mIvMenu = findViewById(R.id.iv_menu);
        mMToolbarTitle = findViewById(R.id.mToolbarTitle);
        mDropDownMenu = findViewById(R.id.dropDownMenu);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            moreInfoShowAdapter.notifyDataSetChanged();
            handler.postDelayed(this, 1000);
        }
    };

}
