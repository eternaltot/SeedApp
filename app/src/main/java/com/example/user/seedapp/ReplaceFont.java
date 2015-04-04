package com.example.user.seedapp;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by LacNoito on 4/2/2015.
 */
public class ReplaceFont {

    public static void replaceDefaultFont(Context context,
                                          String nameOfFontBeginReplaced,
                                          String nameOfFontInAsset){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), nameOfFontInAsset);
        replaceFont(nameOfFontBeginReplaced, typeface);
    }

    private static void replaceFont(String nameOfFontBeginReplaced, Typeface typeface){
        try {
            Field field = Typeface.class.getDeclaredField(nameOfFontBeginReplaced);
            field.setAccessible(true);
            field.set(null, typeface);
        }catch (Exception e){

        }
    }
}
