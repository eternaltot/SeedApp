package com.example.user.seedapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.seedapp.com.add.model.Banner;

/**
 * Created by TOT on 9/3/2558.
 */
public class FragmentBanner extends Fragment {
    private Banner banner;
    private static View view;

    public void setBanner(Banner banner){
        this.banner = banner;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try {
            View view = inflater.inflate(R.layout.fragment_banner, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            imageView.setImageBitmap(banner.getBitmap());

            return view;
        }catch (Exception e){

        }
        return view;
    }
}
