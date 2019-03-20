package com.example.my.labelmanagement;

import android.os.Bundle;

import com.shizhefei.fragment.LazyFragment;

/**
 * 读取界面
 * @author 张智超
 */
public class ReadFragment extends LazyFragment {

    public ReadFragment(){

    }

    public ReadFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }
}
