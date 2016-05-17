package com.zshgif.laugh.bean;

/**
 * Created by Administrator on 2016/5/15.
 * 评论对象
 */
public class CommentsBean {

    private String id;
    /**
     * 评论人名称
     */
    private String commentUserName;
    /**
     * 评论人头像
     */
    private String commentUserProfile;

    /**
     * 评论
     */
    private String comment;

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserProfile() {
        return commentUserProfile;
    }

    public void setCommentUserProfile(String commentUserProfile) {
        this.commentUserProfile = commentUserProfile;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
