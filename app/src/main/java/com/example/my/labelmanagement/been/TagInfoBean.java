package com.example.my.labelmanagement.been;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 批量写入校验数据库
 *
 */
@Entity
public class TagInfoBean  implements Parcelable{
    @Id(autoincrement = false)
    private String SN;//条码特征信息

    private String GroupInformation;//分组信息
    private String Model;//设备型号
    private String Type;//设备类型
    private String Vendor;//厂商
    private String OccupiedHeight;//占用U位高度
    private String Lifecycle;//生命周期
    private String FirstUse;//首次使用
    private String Weight;//重量
    private String RatedPower;//额定功率
    private String Owner;//资产所有人
    private String PartNum;//部件号
    private String fileName;//文件名
    private boolean isWrite = false;//是否写入
    private boolean isCheck = false;//是否校验


    @Generated(hash = 1419712651)
    public TagInfoBean(String SN, String GroupInformation, String Model,
            String Type, String Vendor, String OccupiedHeight, String Lifecycle,
            String FirstUse, String Weight, String RatedPower, String Owner,
            String PartNum, String fileName, boolean isWrite, boolean isCheck) {
        this.SN = SN;
        this.GroupInformation = GroupInformation;
        this.Model = Model;
        this.Type = Type;
        this.Vendor = Vendor;
        this.OccupiedHeight = OccupiedHeight;
        this.Lifecycle = Lifecycle;
        this.FirstUse = FirstUse;
        this.Weight = Weight;
        this.RatedPower = RatedPower;
        this.Owner = Owner;
        this.PartNum = PartNum;
        this.fileName = fileName;
        this.isWrite = isWrite;
        this.isCheck = isCheck;
    }


    @Generated(hash = 1692947207)
    public TagInfoBean() {
    }


    protected TagInfoBean(Parcel in) {
        SN = in.readString();
        GroupInformation = in.readString();
        Model = in.readString();
        Type = in.readString();
        Vendor = in.readString();
        OccupiedHeight = in.readString();
        Lifecycle = in.readString();
        FirstUse = in.readString();
        Weight = in.readString();
        RatedPower = in.readString();
        Owner = in.readString();
        PartNum = in.readString();
        fileName = in.readString();
        isWrite = in.readByte() != 0;
        isCheck = in.readByte() != 0;
    }

    public static final Creator<TagInfoBean> CREATOR = new Creator<TagInfoBean>() {
        @Override
        public TagInfoBean createFromParcel(Parcel in) {
            return new TagInfoBean(in);
        }

        @Override
        public TagInfoBean[] newArray(int size) {
            return new TagInfoBean[size];
        }
    };

    @Override
    public String toString() {
        return "TagInfoBean{" +
                "SN='" + SN + '\'' +
                ", GroupInformation='" + GroupInformation + '\'' +
                ", Model='" + Model + '\'' +
                ", Type='" + Type + '\'' +
                ", Vendor='" + Vendor + '\'' +
                ", OccupiedHeight='" + OccupiedHeight + '\'' +
                ", Lifecycle='" + Lifecycle + '\'' +
                ", FirstUse='" + FirstUse + '\'' +
                ", Weight='" + Weight + '\'' +
                ", RatedPower='" + RatedPower + '\'' +
                ", Owner='" + Owner + '\'' +
                ", PartNum='" + PartNum + '\'' +
                ", fileName='" + fileName + '\'' +
                ", isWrite=" + isWrite +
                ", isCheck=" + isCheck +
                '}';
    }


    public String getSN() {
        return this.SN;
    }


    public void setSN(String SN) {
        this.SN = SN;
    }


    public String getGroupInformation() {
        return this.GroupInformation;
    }


    public void setGroupInformation(String GroupInformation) {
        this.GroupInformation = GroupInformation;
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


    public String getFirstUse() {
        return this.FirstUse;
    }


    public void setFirstUse(String FirstUse) {
        this.FirstUse = FirstUse;
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


    public String getFileName() {
        return this.fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public boolean getIsWrite() {
        return this.isWrite;
    }


    public void setIsWrite(boolean isWrite) {
        this.isWrite = isWrite;
    }


    public boolean getIsCheck() {
        return this.isCheck;
    }


    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SN);
        dest.writeString(GroupInformation);
        dest.writeString(Model);
        dest.writeString(Type);
        dest.writeString(Vendor);
        dest.writeString(OccupiedHeight);
        dest.writeString(Lifecycle);
        dest.writeString(FirstUse);
        dest.writeString(Weight);
        dest.writeString(RatedPower);
        dest.writeString(Owner);
        dest.writeString(PartNum);
        dest.writeString(fileName);
        dest.writeByte((byte) (isWrite ? 1 : 0));
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }
}
