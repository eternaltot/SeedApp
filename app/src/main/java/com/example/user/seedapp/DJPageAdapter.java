package com.example.user.seedapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by LacNoito on 2/22/2015.
 */
public class DJPageAdapter extends FragmentPagerAdapter {

    private JSONArray dj_info_array;

    public DJPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public DJPageAdapter(FragmentManager fm, JSONArray dj_info_array) {
        super(fm);

        this.dj_info_array = dj_info_array;
    }

    @Override
    public Fragment getItem(int i) {
        try {
            JSONObject object =  dj_info_array.getJSONObject(i);

            FragmentDJIndoPage fragmentDJIndoPage = new FragmentDJIndoPage();
            fragmentDJIndoPage.setJsonObject(object);

            return fragmentDJIndoPage;
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public int getCount() {
        return dj_info_array.length();
    }
}
