package com.example.user.seedapp;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.seedapp.com.add.view.AutoScrollViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import android.os.Handler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by LacNoito on 2/10/2015.
 */
public class FragmentMain extends Fragment {

    private static View view;
    private Button bt_youtube;
    private Button bt_lyrics;
    private Button bt_play;
    private MainActivity mainActivity;
    private PlayMedia play;
    private static int SPLASH_TIMEOUT = 3000;

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

//            bt_play.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (bt_play.getText().equals("Play")){
//                        bt_play.setBackgroundColor(Color.WHITE);
//                        bt_play.setTextColor(Color.BLACK);
//                        bt_play.setText("Pause");
//                    }else {
//                        bt_play.setBackgroundColor(Color.BLACK);
//                        bt_play.setTextColor(Color.WHITE);
//                        bt_play.setText("Play");
//                    }
//                }
//            });

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

            new PlayMediaTask().execute();


        }catch (Exception e){

        }
        return view;
    }

    public void getDateListFromServer(){
        URL url = null;
        try {
            url = new URL("http://192.168./testServer/");
            URLConnection conn = url.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("POST");
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            String parsedString = mainActivity.convertinputStreamToString(is);

            Log.e("system", "DATA List" + parsedString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataList(Boolean flag){
        if(flag){
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    getDateListFromServer();
                    getDataList(true);
                }
            }, SPLASH_TIMEOUT);
        }else{
            getDateListFromServer();
        }
    }

    class PlayMediaTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            play = new PlayMedia(mainActivity.getURL(), mainActivity.returnBaseContext());

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {

                    bt_play.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            if (bt_play.getText().equals("Play")){
                                bt_play.setBackgroundColor(Color.WHITE);
                                bt_play.setTextColor(Color.BLACK);
                                bt_play.setText("Pause");

//                                MediaPlayer mediaPlayer = MediaPlayer.create(mainActivity.returnBaseContext(), R.raw.body_slam);
//                                mediaPlayer.start();
                                play.playMedia(true);
                            }else {
                                bt_play.setBackgroundColor(Color.BLACK);
                                bt_play.setTextColor(Color.WHITE);
                                bt_play.setText("Play");

                                play.playMedia(false);
                            }
                        }
                    });
                }
            });
        }
    }


    class getDataListTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    getDataList(false);
                }
            });
        }
    }
}
