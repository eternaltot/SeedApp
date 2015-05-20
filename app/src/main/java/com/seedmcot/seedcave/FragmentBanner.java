package com.seedmcot.seedcave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seedmcot.seedcave.add.model.Banner;

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
            View view = null;
                 view = inflater.inflate(R.layout.fragment_banner, container, false);

                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

                imageView.setImageBitmap(banner.getBitmap());
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context mainActivity = getActivity();
                        Intent intent = new Intent(mainActivity,WebviewActivity.class);
                        intent.putExtra("URL", banner.getUrl());
//                        Bundle bundle = ActivityOptions.makeCustomAnimation(mainActivity, R.anim.slide_in_up, R.anim.slide_out_up).toBundle();
//                        mainActivity.startActivity(intent, bundle);
                        mainActivity.startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                });

            return view;
        }catch (Exception e){
            Log.e("system",e.getMessage());
        }
        return view;
    }
}
