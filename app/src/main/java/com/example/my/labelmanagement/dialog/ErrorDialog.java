package com.example.my.labelmanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.utils.FileUtils;
import com.example.my.labelmanagement.utils.xls.DateUtils;

/**
 * @author 张智超
 * @date 2019/4/14
 */
public class ErrorDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private String mError;
    private TextView mTvFile;
    private Button mBtnSure;
    private String path;
    private String title;
    private TextView mTvTitle;

    public ErrorDialog(@NonNull Context context, String error, String path, String title) {
        super(context);
        this.mContext = context;
        this.mError = error;
        this.path = path;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_error);
        initView();
        saveFile();
    }

    private void saveFile() {
        String dirPath = Environment.getExternalStorageDirectory() + path;
        FileUtils.createOrExistsDir(dirPath);
        String currentTimeMillis = DateUtils.getCurrentTimeMillis(DateUtils.FORMAT_YMDHMS) + ".txt";
        boolean writeFile = FileUtils.writeFile(dirPath + currentTimeMillis, mError);
        if (writeFile) {
            mTvFile.setText(path + currentTimeMillis);
        } else {
            mTvFile.setText(R.string.save_failed);
        }
    }

    private void initView() {
        mTvFile = (TextView) findViewById(R.id.tv_file);
        mBtnSure = (Button) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                break;
        }
    }
}
