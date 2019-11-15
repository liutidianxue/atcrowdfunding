package com.atguigu.atcrowdfunding.bean.ex;

import com.atguigu.atcrowdfunding.bean.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hewei
 * @date 2019/10/2 - 19:16
 */
public class PermissionEx extends Permission {
    //必须要有name，open和children属性，因为前台Ztree封装的属性就是这些
    private boolean open;

    private boolean checked ;

    private int level ;

    //List<PermissionEx> childrenEx = new ArrayList<>();

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


    /*public List<PermissionEx> getChildrenEx() {
        return childrenEx;
    }

    public void setChildrenEx(List<PermissionEx> childrenEx) {
        this.childrenEx = childrenEx;
    }*/
}
