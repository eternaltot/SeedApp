package com.seedmcot.seedcave.add.model;

import android.graphics.Bitmap;

/**
 * Created by TOT on 9/3/2558.
 */
public class Banner {
    private Bitmap bitmap;
    private String url;

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
