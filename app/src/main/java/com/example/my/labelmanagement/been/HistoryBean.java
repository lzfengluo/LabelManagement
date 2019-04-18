package com.example.my.labelmanagement.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 历史记录表
 */
@Entity
public class HistoryBean {
    @Id(autoincrement = false)
    private String time;
    private String fileName;
    private int addNum;
    private int upDataNum;
    private int delNum;
    @Generated(hash = 1228513445)
    public HistoryBean(String time, String fileName, int addNum, int upDataNum,
            int delNum) {
        this.time = time;
        this.fileName = fileName;
        this.addNum = addNum;
        this.upDataNum = upDataNum;
        this.delNum = delNum;
    }
    @Generated(hash = 48590348)
    public HistoryBean() {
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public int getAddNum() {
        return this.addNum;
    }
    public void setAddNum(int addNum) {
        this.addNum = addNum;
    }
    public int getUpDataNum() {
        return this.upDataNum;
    }
    public void setUpDataNum(int upDataNum) {
        this.upDataNum = upDataNum;
    }
    public int getDelNum() {
        return this.delNum;
    }
    public void setDelNum(int delNum) {
        this.delNum = delNum;
    }
}
