package com.zshgif.laugh.bean;

/**
 * 图片发布人
 * Created by Administrator on 2016/5/15.
 */
public class ReleaseUser {
    /**
     * 会员头像
     */
    private String userProfile;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 头像Key
     */
    private String userProfileKey;

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

    public String getUserProfileKey() {
        return userProfileKey;
    }

    public void setUserProfileKey(String userProfileKey) {
        this.userProfileKey = userProfileKey;
    }
}
