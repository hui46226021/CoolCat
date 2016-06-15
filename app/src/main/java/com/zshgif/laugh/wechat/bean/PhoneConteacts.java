package com.zshgif.laugh.wechat.bean;

/**
 * Created by zhush on 2016/6/15.
 * 手机联系人
 */
public class PhoneConteacts {
    private String phone;//手机号
    private String name;//名字
    private boolean state;//状态

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


    public PhoneConteacts(String phone, String name, boolean state) {
        this.phone = phone;
        this.name = name;
        this.state = state;
    }

    @Override
    public String toString() {
        return "PhoneConteacts{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
