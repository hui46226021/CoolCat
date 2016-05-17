package com.zshgif.laugh.bean;

/**
 * 图片对象
 * Created by Administrator on 2016/5/15.
 */
public class PictureBean {
    private String id ;
    /**
     * 大图
     */
    private  String largeImage;

    /**
     * 缩略图
     */
    private  String thumbImage;


    public String getLargeImage() {
        return largeImage;
    }



    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }



    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }



    public PictureBean(String largeImage, String largeImageKey, String thumbImage, String thumbImageKey) {
        this.largeImage = largeImage;
        this.thumbImage = thumbImage;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }
}
