package com.example.my.labelmanagement.been;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 *
 */
public class MoreInfoShowBean extends SectionEntity<TagInfoBean> {
    private TagInfoBean tagInfoBean;
    private int number;
    public MoreInfoShowBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MoreInfoShowBean(boolean isHeader, String header,TagInfoBean tagInfoBean,int number) {
        super(isHeader, header);
        this.tagInfoBean=tagInfoBean;
        this.number=number;
    }

    public TagInfoBean getTagInfoBean() {
        return tagInfoBean;
    }

    public void setTagInfoBean(TagInfoBean tagInfoBean) {
        this.tagInfoBean = tagInfoBean;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
