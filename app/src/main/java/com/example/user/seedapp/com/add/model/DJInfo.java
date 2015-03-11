package com.example.user.seedapp.com.add.model;

import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by LacNoito on 2/24/2015.
 */
public class DJInfo {
    private JSONObject JSONObject;
    private Bitmap bitmap;
    private Integer djId;
    private String startTime;
    private String stopTime;
    private String name;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getStopTime(){
        return this.stopTime;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public void setStopTime(String stopTime){
        this.stopTime = stopTime;
    }

    public void setDjId(Integer djId){
        this.djId = djId;
    }

    public Integer getDjId(){
        return this.djId;
    }

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
