package com.example.my.labelmanagement.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 记录导入过的表格名称
 *
 */
@Entity
public class MoreWriteFileName {
    @Id(autoincrement = false)
    String name;

    @Generated(hash = 386878321)
    public MoreWriteFileName(String name) {
        this.name = name;
    }

    @Generated(hash = 1998228573)
    public MoreWriteFileName() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
