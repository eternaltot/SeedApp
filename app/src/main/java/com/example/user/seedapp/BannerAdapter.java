package com.example.user.seedapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.seedapp.com.add.model.Banner;

import org.json.JSONArray;
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

    private ArrayList<String> list_url_banner;
    private ArrayList<Banner> list_banner = new ArrayList<Banner>();

    public BannerAdapter(FragmentManager fm,ArrayList<String> list_url_banner) throws IOException {
        super(fm);
        this.list_url_banner = list_url_banner;
        for(String s : list_url_banner){
            URL newurl = new URL(s);
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            Banner banner = new Banner();
            banner.setBitmap(mIcon_val);
            list_banner.add(banner);
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

