package com.zshgif.laugh.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PICTUREBEAN.
 */
public class PictureBean {

    private Long id;
    /** Not-null value. */
    /**
     * 大图
     */
    private String largeImage;
    /** Not-null value. */
    /**
     * 小图
     */
    private String thumbImage;
    private long large_image_listId;

    public PictureBean() {
    }

    public PictureBean(Long id) {
        this.id = id;
    }

    public PictureBean(Long id, String largeImage, String thumbImage, long large_image_listId) {
        this.id = id;
        this.largeImage = largeImage;
        this.thumbImage = thumbImage;
        this.large_image_listId = large_image_listId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getLargeImage() {
        return largeImage;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    /** Not-null value. */
    public String getThumbImage() {
        return thumbImage;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public long getLarge_image_listId() {
        return large_image_listId;
    }

    public void setLarge_image_listId(long large_image_listId) {
        this.large_image_listId = large_image_listId;
    }

}