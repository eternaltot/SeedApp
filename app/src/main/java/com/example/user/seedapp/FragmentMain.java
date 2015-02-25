package com.example.user.seedapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.seedapp.com.add.view.AutoScrollViewPager;
import com.viewpagerindicator.CirclePageIndicator;


/**
 * Created by LacNoito on 2/10/2015.
 */
public class FragmentMain extends Fragment {

    private static View view;
    private Button bt_youtube;
    private Button bt_lyrics;
    private Button bt_play;
    private MainActivity mainActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_main, container, false);

            mainActivity = (MainActivity)getActivity();

            DJPageAdapter adapter = new DJPageAdapter(mainActivity.getSupportFragmentManager(), mainActivity.getDJInfos());
            AutoScrollViewPager pager = (AutoScrollViewPager) view.findViewById(R.id.pager);
            pager.setAdapter(adapter);
            pager.startAutoScroll(5);
            pager.setInterval(2500);

            CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
            indicator .setViewPager(pager);
            indicator.setSnap(true);

            bt_youtube = (Button) view.findViewById(R.id.bt_youtube);
            bt_lyrics = (Button) view.findViewById(R.id.bt_lyrics);
            bt_play = (Button) view.findViewById(R.id.bt_play);

            bt_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bt_play.getText().equals("Play")){
                        bt_play.setBackgroundColor(Color.WHITE);
                        bt_play.setTextColor(Color.BLACK);
                        bt_play.setText("Pause");
                    }else {
                        bt_play.setBackgroundColor(Color.BLACK);
                        bt_play.setTextColor(Color.WHITE);
                        bt_play.setText("Play");
                    }
                }
            });

            bt_youtube.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FragmentYouTube fragmentYouTube = new FragmentYouTube();
                    fragmentYouTube.setYoutubeName("VT8uLDauarc");
                    mainActivity.setFragment(fragmentYouTube);
                }
            });

            bt_lyrics.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FragmentLyrics fragmentLyrics = new FragmentLyrics();
                    mainActivity.setFragment(fragmentLyrics);
                }
            });


        }catch (Exception e){

        }
        return view;
    }

}
