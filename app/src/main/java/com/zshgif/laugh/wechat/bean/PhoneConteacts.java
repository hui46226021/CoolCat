package com.zshgif.laugh.wechat.bean;

/**
 * Created by zhush on 2016/6/15.
 * 手机联系人
 */
public class PhoneConteacts {
    private String phone;//手机号
    private String name;//名字
    private int state;//状态  0开启  1关闭 2 已添加
    private String  avatar;//头像
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public PhoneConteacts(String phone, String name, int state) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
