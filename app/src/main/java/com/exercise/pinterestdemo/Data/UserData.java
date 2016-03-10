package com.exercise.pinterestdemo.Data;

import java.io.Serializable;

/**
 *
 */
public class UserData implements Serializable{

    private static final long serialVersionUID = 1L;
    private String userId="";
    private String username="";
    private String profileImageURL="";
    private int pinCount=0;

    public UserData() {
    }

    public UserData(String userId, String username, String profileImageURL, int pinCount) {
        this.userId = userId;
        this.username = username;
        this.profileImageURL = profileImageURL;
        this.pinCount = pinCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public int getPinCount() {
        return pinCount;
    }

    public void setPinCount(int pinCount) {
        this.pinCount = pinCount;
    }


}
