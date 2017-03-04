package com.codepath.apps.restclienttemplate.models;

import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Tuan on 2017/03/04.
 */

public class Tweet extends BaseModel{
    private String text;
    private User user;
    private String createdAt;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
