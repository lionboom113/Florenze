package com.codepath.apps.restclienttemplate.models;

/**
 * Created by Tuan on 2017/03/04.
 */

public class User {
    private String name;
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
