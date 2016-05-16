package com.zshgif.laugh.bean;

import java.util.List;

/**
 * 图片项目实体类
 * Created by Administrator on 2016/5/10.
 */
public class GifitemBean {
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
     * 第一张图地址
     */
    private String firstOne;
    /**
     * 第一张图缓存Key
     */
    private String firstOneKey;
    /**
     * gif图地址
     */
    private String gifUrl;
    /**
     * gif图key
     */
    private String gifUrlKey;

    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * 多图图片集合
     */
    private List<PictureBean> large_image_list;
    /**
     * 图片类型 1 单张图片  3 单张GIF  5 多图
     */
    private int type;
    /**
     * 评论者
     */
    private CommentsBean comments;
    /**
     * 发布者
     */
    private ReleaseUser releaseUser;







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

    public String getFirstOne() {
        return firstOne;
    }

    public void setFirstOne(String firstOne) {
        this.firstOne = firstOne;
    }

    public String getFirstOneKey() {
        return firstOneKey;
    }

    public void setFirstOneKey(String firstOneKey) {
        this.firstOneKey = firstOneKey;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getGifUrlKey() {
        return gifUrlKey;
    }

    public void setGifUrlKey(String gifUrlKey) {
        this.gifUrlKey = gifUrlKey;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<PictureBean> getLarge_image_list() {
        return large_image_list;
    }

    public void setLarge_image_list(List<PictureBean> large_image_list) {
        this.large_image_list = large_image_list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
