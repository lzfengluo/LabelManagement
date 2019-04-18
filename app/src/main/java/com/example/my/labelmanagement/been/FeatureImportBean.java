package com.example.my.labelmanagement.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 设备特征数据表
 */
@Entity
public class FeatureImportBean {
    @Id(autoincrement = false)
    private String SN;//序列号

    private String OperationType;//操作类型
    private String Model;//设备型号
    private String Type;//设备类型
    private String Vendor;//厂商
    private String OccupiedHeight;//占用U位高度
    private String Lifecycle;//生命周期
    private String Weight;//重量
    private String RatedPower;//额定功率
    private String Owner;//资产所有人
    private String PartNum;//部件号
    @Generated(hash = 1352459867)
    public FeatureImportBean(String SN, String OperationType, String Model,
            String Type, String Vendor, String OccupiedHeight, String Lifecycle,
            String Weight, String RatedPower, String Owner, String PartNum) {
        this.SN = SN;
        this.OperationType = OperationType;
        this.Model = Model;
        this.Type = Type;
        this.Vendor = Vendor;
        this.OccupiedHeight = OccupiedHeight;
        this.Lifecycle = Lifecycle;
        this.Weight = Weight;
        this.RatedPower = RatedPower;
        this.Owner = Owner;
        this.PartNum = PartNum;
    }
    @Generated(hash = 1406193985)
    public FeatureImportBean() {
    }
    public String getSN() {
        return this.SN;
    }
    public void setSN(String SN) {
        this.SN = SN;
    }
    public String getOperationType() {
        return this.OperationType;
    }
    public void setOperationType(String OperationType) {
        this.OperationType = OperationType;
    }
    public String getModel() {
        return this.Model;
    }
    public void setModel(String Model) {
        this.Model = Model;
    }
    public String getType() {
        return this.Type;
    }
    public void setType(String Type) {
        this.Type = Type;
    }
    public String getVendor() {
        return this.Vendor;
    }
    public void setVendor(String Vendor) {
        this.Vendor = Vendor;
    }
    public String getOccupiedHeight() {
        return this.OccupiedHeight;
    }
    public void setOccupiedHeight(String OccupiedHeight) {
        this.OccupiedHeight = OccupiedHeight;
    }
    public String getLifecycle() {
        return this.Lifecycle;
    }
    public void setLifecycle(String Lifecycle) {
        this.Lifecycle = Lifecycle;
    }
    public String getWeight() {
        return this.Weight;
    }
    public void setWeight(String Weight) {
        this.Weight = Weight;
    }
    public String getRatedPower() {
        return this.RatedPower;
    }
    public void setRatedPower(String RatedPower) {
        this.RatedPower = RatedPower;
    }
    public String getOwner() {
        return this.Owner;
    }
    public void setOwner(String Owner) {
        this.Owner = Owner;
    }
    public String getPartNum() {
        return this.PartNum;
    }
    public void setPartNum(String PartNum) {
        this.PartNum = PartNum;
    }
}
