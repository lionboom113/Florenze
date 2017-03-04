package com.codepath.apps.restclienttemplate.models;

import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Tuan on 2017/03/04.
 */

public class Tweet extends BaseModel{
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
