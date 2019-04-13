package com.example.my.labelmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.shizhefei.fragment.LazyFragment;

import java.util.Objects;

/**
 * 写入主界面
 *
 * @author 张智超
 * @date 2019/2/27
 */
public class WriteFragment extends LazyFragment {

    private Intent intent = new Intent();

    public WriteFragment() {

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

    public void initView() {
        //单个写入
        LinearLayout mLlWriteSingle = (LinearLayout) findViewById(R.id.ll_write_single);
        mLlWriteSingle.setOnClickListener(new OnClickWrite());
        //批量写入
        LinearLayout mLlWriteMore = (LinearLayout) findViewById(R.id.ll_write_more);
        mLlWriteMore.setOnClickListener(new OnClickWrite());
    }

    /**
     * 创建独立类实现控件监听事件
     */
    public class OnClickWrite implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_write_single:
                    intent.setClass(Objects.requireNonNull(getActivity()), WriteActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_write_more:
                    intent.setClass(Objects.requireNonNull(getActivity()), NothingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

}


