package com.example.user.seedapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.seedapp.com.add.model.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by TOT on 9/3/2558.
 */
public class BannerAdapter extends FragmentPagerAdapter {

    private JSONArray list_url_banner;
    private ArrayList<Banner> list_banner = new ArrayList<Banner>();

    public BannerAdapter(FragmentManager fm,JSONArray list_url_banner) throws IOException, JSONException {
        super(fm);
        this.list_url_banner = list_url_banner;

        if(list_url_banner != null){
            for(int i = 0 ;i<list_url_banner.length();++i) {
                JSONObject obj = list_url_banner.getJSONObject(i);
                String s = MainActivity.path_Image_Topbanner + (String) obj.get("image");
                String url = (String) obj.get("url_web");
                URL newurl = new URL(s);
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                Banner banner = new Banner();
                banner.setBitmap(mIcon_val);
                banner.setUrl(url != null && !url.isEmpty() ? url : "http://www.google.com");
                list_banner.add(banner);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        Banner banner = list_banner.get(position);
        FragmentBanner fragmentBanner = new FragmentBanner();
        fragmentBanner.setBanner(banner);
        return fragmentBanner;
    }

    @Override
    public int getCount() {
        return list_banner.size();
    }
}

