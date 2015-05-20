package com.seedmcot.seedcave.add.model;

import android.graphics.Bitmap;

/**
 * Created by User on 11/3/2558.
 */
public class Privilege_Child {
    private String url;
    private Bitmap bitmap;
    private String urlImage;

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
