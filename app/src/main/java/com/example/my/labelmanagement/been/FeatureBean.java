package com.example.my.labelmanagement.been;

import java.util.List;

/**
 *
 */
public class FeatureBean {
    private String name;
    private boolean isImport;
    private int num;
    private List<FeatureImportBean> lists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImport() {
        return isImport;
    }

    public void setImport(boolean anImport) {
        isImport = anImport;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<FeatureImportBean> getLists() {
        return lists;
    }

    public void setLists(List<FeatureImportBean> lists) {
        this.lists = lists;
    }
}
