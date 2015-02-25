package com.example.user.seedapp.com.add.model;

import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by LacNoito on 2/24/2015.
 */
public class DJInfo {
    private JSONObject JSONObject;
    private Bitmap bitmap;

    public void setJSONObject(JSONObject JSONObject){
        this.JSONObject = JSONObject;
    }

    public JSONObject getJSONObject(){
        return this.JSONObject;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }
}
