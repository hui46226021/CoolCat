package com.zshgif.laugh.bean;

/**
 * 图片对象
 * Created by Administrator on 2016/5/15.
 */
public class PictureBean {
    /**
     * 大图
     */
    private  String largeImage;
    /**
     * 大图key
     */
    private  String largeImageKey;
    /**
     * 缩略图
     */
    private  String thumbImage;
    /**
     * 缩略图key
     */
    private  String thumbImageKey;

    public String getLargeImage() {
        return largeImage;
    }



    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getLargeImageKey() {
        return largeImageKey;
    }

    public void setLargeImageKey(String largeImageKey) {
        this.largeImageKey = largeImageKey;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getThumbImageKey() {
        return thumbImageKey;
    }

    public void setThumbImageKey(String thumbImageKey) {
        this.thumbImageKey = thumbImageKey;
    }

    public PictureBean(String largeImage, String largeImageKey, String thumbImage, String thumbImageKey) {
        this.largeImage = largeImage;
        this.largeImageKey = largeImageKey;
        this.thumbImage = thumbImage;
        this.thumbImageKey = thumbImageKey;
    }
}
