package com.example.my.labelmanagement;

import android.os.Bundle;

import com.shizhefei.fragment.LazyFragment;

/**
 * 写入界面
 */
public class WriteFragment extends LazyFragment {

    public WriteFragment(){

    }

    public WriteFragment setArguments(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("_type", type);
        setArguments(bundle);
        return this;
    }
}


