package com.example.user.seedapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by LacNoito on 2/25/2015.
 */
public class FragmentLive extends Fragment {
    private static View view;
    private static String youtubeName = "l8Iu9PEKMmw";
    private static String musicName = "-";
    private YouTubePlayer YPlayer;
    private ArrayList<String> listitem = new ArrayList<String>();
    private static final String YoutubeDeveloperKey = "AIzaSyCjfgiAytO0iYrnz7EQuWarGLSSPmW_mw0";
    private CustomAdapter adapter;

    public void setListData(){
        listitem = new ArrayList<String>();
        listitem.add("VDO1");
        listitem.add("VDO2");
        listitem.add("VDO3");
        listitem.add("VDO4");
        listitem.add("VDO5");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_live, container, false);
            getDataFromServer();
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(tv_name.getText() + musicName);

            ((MainActivity)getActivity()).pauseMediaFromMainActivity();

            ListView expandableListView = (ListView) view.findViewById(R.id.listView);

            final Resources res =getResources();
            setListData();
            adapter = new CustomAdapter(getActivity(),listitem, res,true);
            expandableListView.setAdapter(adapter);
            expandableListView.setDivider(null);

            YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

            youTubePlayerFragment.initialize(YoutubeDeveloperKey, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                    if (!b) {
                        YPlayer = youTubePlayer;
                        YPlayer.setFullscreen(false);
                        YPlayer.loadVideo(youtubeName);
                        YPlayer.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                    // TODO Auto-generated method stub

                }
            });

        }catch (Exception e){

        }

        return view;
    }

    public void getDataFromServer(){
        try {

//            URL url = new URL("http://192.168.43.174/testServer/");
//            URLConnection conn = url.openConnection();
//
//            HttpURLConnection httpConn = (HttpURLConnection) conn;
//            httpConn.setAllowUserInteraction(false);
//            httpConn.setInstanceFollowRedirects(true);
//            httpConn.setRequestMethod("POST");
//            httpConn.connect();
//
//            InputStream is = httpConn.getInputStream();
//            String parsedString = convertinputStreamToString(is);
//
//            JSONObject jsonObject = new JSONObject(parsedString);

            String parsedString = "{\"youtube\":{\"name_music\":\"คิดถึง - bodyslam\",\"youtube_name\":\"_3kZzIfTt0o\"}}";
            JSONObject jsonObject = new JSONObject(parsedString);
            JSONObject youtube = (JSONObject) jsonObject.get("youtube");
            if(youtube != null){
                musicName = youtube.getString("name_music");
                youtubeName = youtube.getString("youtube_name");
            }


//            for(int x= 0 ; x < dj_info_array.length() ; ++x){
//                JSONObject object = dj_info_array.getJSONObject(x);
//                Log.d("system", object.getString("name"));
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertinputStreamToString(InputStream ists)
            throws IOException {
        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(
                        ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
