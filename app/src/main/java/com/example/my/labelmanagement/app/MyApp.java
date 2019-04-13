package com.example.my.labelmanagement.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.my.labelmanagement.been.DaoMaster;
import com.example.my.labelmanagement.been.DaoSession;
import com.example.my.labelmanagement.utils.FileUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

public class MyApp extends Application {
    private static MyApp m_application;
    //greendao
    private static DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        m_application = this;
        setupDatabase();
        CrashReport.initCrashReport(getApplicationContext(), "de36b59503", false);

        FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/feature/");
        FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/feature/log/");
        FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/tag/");
        FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory() + File.separator + "LabelRFID/tag/log/");
    }


    public static MyApp getInstance() {
        return m_application;
    }

    private void setupDatabase() {
        //创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "greendao.db", null);
        //获得可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获得数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获得dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
