package com.example.user.seedapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by LacNoito on 2/22/2015.
 */
public class FragmentDJIndoPage extends Fragment {

    private JSONObject jsonObject;
    private static View view;

    public void setJsonObject(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }

        try {
            View view = inflater.inflate(R.layout.dj_infomation_page, container, false);

            TextView nameDJ = (TextView) view.findViewById(R.id.name_dj);
            TextView online_time = (TextView) view.findViewById(R.id.online_time);
            ImageView imageDJ = (ImageView) view.findViewById(R.id.imageDJ);

            nameDJ.setText(nameDJ.getText().toString() + " " + jsonObject.getString("name"));
            online_time.setText(jsonObject.getString("time_online"));

            URL newurl = new URL(jsonObject.getString("image"));
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            imageDJ.setImageBitmap(mIcon_val);

            return view;
        }catch (Exception e){

        }
        return view;
    }
}
