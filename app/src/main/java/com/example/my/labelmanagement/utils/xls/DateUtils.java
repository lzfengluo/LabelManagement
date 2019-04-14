package com.example.my.labelmanagement.utils.xls;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {
    public static String FORMAT_YMDHMS = "yyyy/MM/dd HH:mm";
    public static String FORMAT_YMD = "yyyy/MM/dd";

    public static Date parse(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
        try {
            return simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }


    }

    public static String format(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String format(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前日期
     *
     * @param format 日期格式
     * @return String
     */
    public static String getCurrentTimeMillis(String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        return dateFormat.format(date);
    }
}
