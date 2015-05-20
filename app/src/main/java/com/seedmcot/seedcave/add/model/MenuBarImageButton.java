package com.seedmcot.seedcave.add.model;

import android.graphics.Bitmap;
import android.widget.ImageButton;

/**
 * Created by LacNoito on 4/6/2015.
 */
public class MenuBarImageButton {
    private ImageButton imageButton;
    private Bitmap bitmap;

    public MenuBarImageButton(ImageButton imageButton, Bitmap bitmap){
        this.imageButton = imageButton;
        this.bitmap = bitmap;
    }

    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
