package com.example.my.labelmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.my.labelmanagement.adapter.HelpAdapter;
import com.example.my.labelmanagement.adapter.HelpListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助界面
 * Created by 张智超_ on 2019/1/21.
 * Email 981547125@qq.com
 */
public class HelpActivity extends Activity {

    private ImageView mIvMenu;
    private TextView mToolbarTitle;
    private ListView listView;
    private TextView helpTV;
    private List<HelpListUtil> helpListUtils = new ArrayList<HelpListUtil>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
        initData();
    }

    private void initView() {
        mIvMenu = (ImageView) findViewById(R.id.iv_menu);
        mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        listView = (ListView) findViewById(R.id.help_list);
        helpTV = (TextView) findViewById(R.id.help_title);
    }

    private void initData() {
        mIvMenu.setImageResource(R.mipmap.icons_back);
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });
        mToolbarTitle.setText(R.string.menu_help);

        initHelpList();
//        加载自定义适配器
        final HelpAdapter adapter = new HelpAdapter(HelpActivity.this, R.layout.item_help, helpListUtils);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setmCurrentItem(position);
                adapter.notifyDataSetChanged();

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putString("title",helpListUtils.get(position).getHelpTitle());
                bundle.putString("content",helpListUtils.get(position).getHelpContent());
                intent.putExtras(bundle);
                intent.setClass(HelpActivity.this,HelpQuestionActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 添加列表内容
     * */
    private void initHelpList() {
        HelpListUtil helpListUtil1 = new HelpListUtil(getResources().getString(R.string.help_que_title1));
        helpListUtil1.setHelpTitle(getResources().getString(R.string.help_que_title1));
        helpListUtil1.setHelpContent(getResources().getString(R.string.help_que_content1));
        helpListUtils.add(helpListUtil1);
        HelpListUtil helpListUtil2 = new HelpListUtil(getResources().getString(R.string.help_que_title2));
        helpListUtil2.setHelpContent(getResources().getString(R.string.help_que_content2));
        helpListUtils.add(helpListUtil2);
        HelpListUtil helpListUtil3 = new HelpListUtil(getResources().getString(R.string.help_que_title3));
        helpListUtil3.setHelpContent(getResources().getString(R.string.help_que_content3));
        helpListUtils.add(helpListUtil3);
    }

}
