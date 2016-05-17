package com.zshgif.laugh.bean;

import java.util.List;

/**
 * 段子项目实体类
 * Created by Administrator on 2016/5/10.
 */
public class DuanZiBean {
    /**
     * id
     */
    private String id;

    /**
     * 内容
     */
    private String content;
    /**
     * 分类名称
     */
    private String category_name;

    /**
     * 评论者
     */
    private CommentsBean comments;
    /**
     * 发布者
     */
    private ReleaseUser releaseUser;

    /**
     * 赞
     */
    private int digg_count;

    /**
     * 鄙视
     */
    private int bury_count;
    /**
     * 评论数
     */
    private int comments_count;
    /**
     * 分享地址
     */
    private String share_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    public CommentsBean getComments() {
        return comments;
    }

    public void setComments(CommentsBean comments) {
        this.comments = comments;
    }

    public ReleaseUser getReleaseUser() {
        return releaseUser;
    }

    public void setReleaseUser(ReleaseUser releaseUser) {
        this.releaseUser = releaseUser;
    }

    public int getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(int digg_count) {
        this.digg_count = digg_count;
    }

    public int getBury_count() {
        return bury_count;
    }

    public void setBury_count(int bury_count) {
        this.bury_count = bury_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
