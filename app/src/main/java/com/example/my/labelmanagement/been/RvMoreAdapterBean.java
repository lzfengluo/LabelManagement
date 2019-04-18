package com.example.my.labelmanagement.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 记录校验过的表格名称
 *
 */
@Entity
public class RvMoreAdapterBean {
    @Id(autoincrement = false)
    private String name;
    private int allNum;//总数
    private int writeNum;//写入数量
    private boolean isCheckSuccess;//校验是否成功
    private boolean isCheck;//是否校验
    @Generated(hash = 133517823)
    public RvMoreAdapterBean(String name, int allNum, int writeNum,
            boolean isCheckSuccess, boolean isCheck) {
        this.name = name;
        this.allNum = allNum;
        this.writeNum = writeNum;
        this.isCheckSuccess = isCheckSuccess;
        this.isCheck = isCheck;
    }
    @Generated(hash = 845239012)
    public RvMoreAdapterBean() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAllNum() {
        return this.allNum;
    }
    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }
    public int getWriteNum() {
        return this.writeNum;
    }
    public void setWriteNum(int writeNum) {
        this.writeNum = writeNum;
    }
    public boolean getIsCheckSuccess() {
        return this.isCheckSuccess;
    }
    public void setIsCheckSuccess(boolean isCheckSuccess) {
        this.isCheckSuccess = isCheckSuccess;
    }
    public boolean getIsCheck() {
        return this.isCheck;
    }
    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

}
