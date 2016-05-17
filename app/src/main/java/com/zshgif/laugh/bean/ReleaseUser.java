package com.zshgif.laugh.bean;

/**
 * 图片发布人
 * Created by Administrator on 2016/5/15.
 */
public class ReleaseUser {
    private String id;
    /**
     * 会员头像
     */
    private String userProfile;
    /**
     * 用户昵称
     */
    private String username;


    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
