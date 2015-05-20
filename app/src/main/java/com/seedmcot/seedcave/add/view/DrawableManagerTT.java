package com.seedmcot.seedcave.add.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LacNoito on 3/18/2015.
 */
public class DrawableManagerTT {
    private static Map<String, Bitmap> drawableMap;
    private static Map<String, Bitmap> drawableBitMap;

    public DrawableManagerTT() {
        drawableMap = new HashMap<String, Bitmap>();
        drawableBitMap = new HashMap<String, Bitmap>();
    }

    public void putDrawableBitMap(String urlString, Bitmap bitmap){
        drawableBitMap.put(urlString, bitmap);
    }

    public Bitmap drawableBitMap(String urlString) {
        try {
            if (drawableBitMap.containsKey(urlString) && drawableBitMap.get(urlString) != null) {
                return drawableBitMap.get(urlString);
            }

            URL newurl = new URL(urlString);
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());

            drawableBitMap.put(urlString, mIcon_val);
            return mIcon_val;
        }catch (Exception ex){
            Log.e("system", "Error drawableBitMap DrawableManagerTT");
            Log.e("system", ex.getMessage());
        }

        return null;
    }

    public Bitmap getBitMap(String urlString)  {
        try {
            URL newurl = new URL(urlString);
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            return mIcon_val;
        }catch (Exception e){
            return null;
        }
    }

    public void drawableBitMapOnThread(final String urlString) {
        if (drawableBitMap.containsKey(urlString) && drawableBitMap.get(urlString) != null) {
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {

            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                //TODO : set imageView to a "pending" image
                if (!drawableBitMap.containsKey(urlString) || drawableBitMap.get(urlString) == null) {
                    Bitmap newurl = getBitMap(urlString);
                    drawableBitMap.put(urlString, newurl);
                    Message message = handler.obtainMessage(1, newurl);
                    handler.sendMessage(message);
                }
            }
        };
        thread.start();
    }

    public Bitmap fetchDrawable(String urlString) {
        if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }

//        Log.e("system", "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Bitmap drawable = BitmapFactory.decodeStream(is);
//            Drawable drawable = Drawable.createFromStream(is, "src");

            if (drawable != null) {
                drawableMap.put(urlString, drawable);
            } else {
                Log.w(this.getClass().getSimpleName(), "could not get thumbnail");
            }

            return drawable;
        } catch (Exception e) {
            Log.e("system", "fetchDrawable failed", e);
            return null;
        }
    }

    public void fetchOnThread(final String urlString) {
        if (drawableMap.containsKey(urlString)) {
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
//                imageView.setImageDrawable((Bitmap) message.obj);
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                //TODO : set imageView to a "pending" image
                Bitmap drawable = fetchDrawable(urlString);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.start();
    }

    public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
        if (drawableMap.containsKey(urlString)) {
            imageView.setImageBitmap(drawableMap.get(urlString));
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageView.setImageBitmap((Bitmap) message.obj);
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                //TODO : set imageView to a "pending" image
                Bitmap drawable = fetchDrawable(urlString);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.start();
    }

    private InputStream fetch(String urlString) {
        try {
            URL newurl = new URL(urlString);
            return newurl.openConnection().getInputStream();
        }catch (Exception e){
            return null;
        }

//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpGet request = new HttpGet(urlString);
//        HttpResponse response = httpClient.execute(request);
//        return response.getEntity().getContent();
    }
}
