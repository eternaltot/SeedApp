package com.example.user.seedapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.seedapp.com.add.model.ItemLive;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LacNoito on 2/25/2015.
 */
public class FragmentLive extends Fragment {
    private static View view;
    private static String youtubeName = "l8Iu9PEKMmw";
    private static String musicName = "-";
    private static YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyCjfgiAytO0iYrnz7EQuWarGLSSPmW_mw0";
    private LiveAdapter adapter;
    private static YouTubePlayerSupportFragment youTubePlayerFragment;
    private List<ItemLive> itemLiveList = new ArrayList<ItemLive>();
    private MainActivity mainActivity;
    private TextView tv_name;

    public void getDateLiveFromServer(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://api.seedmcot.com/api/lives");
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = mainActivity.convertinputStreamToString(instream);
                Log.d("system", "Sucess!!!!");
                Log.d("system", result);

                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null){
                    for(int x= 0 ; x < jsonArray.length() ; ++x){
                        JSONObject object = jsonArray.getJSONObject(x);

                        Gson gson = new Gson();
                        ItemLive itemLive = gson.fromJson(object.toString(), ItemLive.class);
                        itemLiveList.add(itemLive);
                    }
                }

            } catch (Exception e) {
                Log.e("system", "Error!!!!");
                Log.e("system", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(tv_name.getText() + musicName);

            mainActivity = (MainActivity)getActivity();

            mainActivity.pauseMediaFromMainActivity();

            ListView expandableListView = (ListView) view.findViewById(R.id.listView);

            final Resources res =getResources();
            getDateLiveFromServer();
            adapter = new LiveAdapter(getActivity(), res, itemLiveList);
            expandableListView.setAdapter(adapter);
            expandableListView.setDivider(null);

            expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ItemLive live = itemLiveList.get(position);

                    Toast.makeText(getActivity().getApplicationContext(), "Play " + live.getTitle(), Toast.LENGTH_LONG).show();

                    if(YPlayer != null){
                        String url = live.getUrl();
                        String[] strings = url.split(mainActivity.getCutURLYoutube());
                        if(strings.length > 0) {
                            YPlayer.loadVideo(strings[strings.length - 1]);
                            tv_name.setText("LIVE : " + live.getTitle());
                            if(!YPlayer.isPlaying()){
                                YPlayer.play();
                            }
                        }
                    }
                }
            });

            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
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
}
