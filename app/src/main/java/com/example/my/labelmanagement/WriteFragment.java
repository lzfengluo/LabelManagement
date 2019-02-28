package com.example.my.labelmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.shizhefei.fragment.LazyFragment;

/**
 * 写入主界面
 * created by 张智超 on 2019/2/27
 */
public class WriteFragment extends LazyFragment {

    private LinearLayout mLlWriteSingle;//单个写入
    private LinearLayout mLlWriteMore;//批量写入
    private Intent intent = new Intent();//intent传递对象

    public WriteFragment(){

    }

    public WriteFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_write);
        initView();
    }

    public void initView(){
        mLlWriteSingle = (LinearLayout) findViewById(R.id.ll_write_single);
        mLlWriteSingle.setOnClickListener(new onClickWrite());
        mLlWriteMore = (LinearLayout) findViewById(R.id.ll_write_more);
        mLlWriteMore.setOnClickListener(new onClickWrite());
    }

    // 创建独立类实现控件监听事件
    public class onClickWrite implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_write_single:
                    intent.setClass(getActivity(),NothingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_write_more:
                    intent.setClass(getActivity(),NothingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

}


