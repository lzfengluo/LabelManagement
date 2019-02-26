package com.example.my.labelmanagement;

import android.os.Bundle;

import com.shizhefei.fragment.LazyFragment;

/**
 * 文件管理界面
 */
public class FileFragment extends LazyFragment {

    public FileFragment(){

    }

    public FileFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }
}
