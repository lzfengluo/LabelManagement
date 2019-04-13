package com.example.my.labelmanagement.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 档案人员表
 */
@Entity
public class InfoBean {
    @Id(autoincrement = false)
    String codeID;//主键

    String XH;  //型号
    String LX;  //设备类型
    String CS; //厂商
    String XLH;   //序列号
    String BJH;  //部件号
    String ZYGS; //占用U位个数
    String SMZQ;  //资产生命周期(年)
    String SCSY; //首次使用时间
    String ZL; //重量（Kg）
    String EDGL;  //额定功率（W）
    String ZCSYR; //资产所有人

    @Generated(hash = 2045072604)
    public InfoBean(String codeID, String XH, String LX, String CS, String XLH,
                    String BJH, String ZYGS, String SMZQ, String SCSY, String ZL,
                    String EDGL, String ZCSYR) {
        this.codeID = codeID;
        this.XH = XH;
        this.LX = LX;
        this.CS = CS;
        this.XLH = XLH;
        this.BJH = BJH;
        this.ZYGS = ZYGS;
        this.SMZQ = SMZQ;
        this.SCSY = SCSY;
        this.ZL = ZL;
        this.EDGL = EDGL;
        this.ZCSYR = ZCSYR;
    }

    @Generated(hash = 134777477)
    public InfoBean() {
    }

    @Override
    public String toString() {
        return "InfoBean{" +
                "codeID='" + codeID + '\'' +
                ", XH='" + XH + '\'' +
                ", LX='" + LX + '\'' +
                ", CS='" + CS + '\'' +
                ", XLH='" + XLH + '\'' +
                ", BJH='" + BJH + '\'' +
                ", ZYGS='" + ZYGS + '\'' +
                ", SMZQ='" + SMZQ + '\'' +
                ", SCSY='" + SCSY + '\'' +
                ", ZL='" + ZL + '\'' +
                ", EDGL='" + EDGL + '\'' +
                ", ZCSYR='" + ZCSYR + '\'' +
                '}';
    }

    public String getCodeID() {
        return this.codeID;
    }

    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    public String getXH() {
        return this.XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getLX() {
        return this.LX;
    }

    public void setLX(String LX) {
        this.LX = LX;
    }

    public String getCS() {
        return this.CS;
    }

    public void setCS(String CS) {
        this.CS = CS;
    }

    public String getXLH() {
        return this.XLH;
    }

    public void setXLH(String XLH) {
        this.XLH = XLH;
    }

    public String getBJH() {
        return this.BJH;
    }

    public void setBJH(String BJH) {
        this.BJH = BJH;
    }

    public String getZYGS() {
        return this.ZYGS;
    }

    public void setZYGS(String ZYGS) {
        this.ZYGS = ZYGS;
    }

    public String getSMZQ() {
        return this.SMZQ;
    }

    public void setSMZQ(String SMZQ) {
        this.SMZQ = SMZQ;
    }

    public String getSCSY() {
        return this.SCSY;
    }

    public void setSCSY(String SCSY) {
        this.SCSY = SCSY;
    }

    public String getZL() {
        return this.ZL;
    }

    public void setZL(String ZL) {
        this.ZL = ZL;
    }

    public String getEDGL() {
        return this.EDGL;
    }

    public void setEDGL(String EDGL) {
        this.EDGL = EDGL;
    }

    public String getZCSYR() {
        return this.ZCSYR;
    }

    public void setZCSYR(String ZCSYR) {
        this.ZCSYR = ZCSYR;
    }
}
