package com.zshgif.laugh.wechat.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhush on 2016/6/20.
 */
public class state extends BmobObject {
    String name;//平台名称
    boolean state;//审核状态

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
