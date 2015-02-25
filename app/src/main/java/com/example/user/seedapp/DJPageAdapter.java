package com.example.user.seedapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.seedapp.com.add.model.DJInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LacNoito on 2/22/2015.
 */
public class DJPageAdapter extends FragmentPagerAdapter {

    private JSONArray dj_info_array;
    private List<DJInfo> djInfos = new ArrayList<DJInfo>();

    public DJPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public DJPageAdapter(FragmentManager fm, JSONArray dj_info_array) throws Exception {
        super(fm);

        this.dj_info_array = dj_info_array;
        for(int x= 0 ; x < dj_info_array.length() ; ++x){
            JSONObject jsonObject = dj_info_array.getJSONObject(x);
            DJInfo djInfo = new DJInfo();
            djInfo.setJSONObject(jsonObject);
            URL newurl = new URL(jsonObject.getString("image"));
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            djInfo.setBitmap(mIcon_val);
            djInfos.add(djInfo);
        }
    }

    public DJPageAdapter(FragmentManager fm, List<DJInfo> djInfos) {
        super(fm);

        this.djInfos = djInfos;
    }

    @Override
    public Fragment getItem(int i) {
        try {
            DJInfo object =  djInfos.get(i);

            FragmentDJIndoPage fragmentDJIndoPage = new FragmentDJIndoPage();
            fragmentDJIndoPage.setObject(object);

            return fragmentDJIndoPage;
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public int getCount() {
        return djInfos.size();
    }
}
