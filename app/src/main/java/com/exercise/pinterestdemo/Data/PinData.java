package com.exercise.pinterestdemo.Data;


import java.io.Serializable;

public class PinData implements Serializable{

    private static final long serialVersionUID = 1L;
    private String pinId ="";
    private String imgURL="";
    private String description="";
    private int width=0;
    private int height=0;


    public PinData() {
    }

    public PinData(String pinId, String imgURL, String description, int width, int height) {
        this.pinId = pinId;
        this.imgURL = imgURL;
        this.description = description;
        this.width = width;
        this.height = height;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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



}
