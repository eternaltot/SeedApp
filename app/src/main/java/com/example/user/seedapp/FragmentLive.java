package com.example.user.seedapp;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
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
    private String list_type;
    private static StreamFragment streamFragment;
    private ItemLive live;
    private TextView textView4;

    public void setYouTubePlayerFragment(YouTubePlayer y){
        YPlayer = y;
    }

    @Override
    public void onDestroyView() {
        youTubePlayerFragment = null;
        YPlayer = null;
        super.onDestroyView();
    }

    public void getDateLiveFromServer(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(mainActivity.list_lives);
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

    public void setBackEvent(){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i("system", "keyCode: " + keyCode);
//                if( keyCode == KeyEvent.KEYCODE_BACK ) {
//                    Log.d("system", "onKey Back listener is working!!!");
//                    mainActivity.setFragmentNoBack(mainActivity.getFragmentMain());
//                    return true;
//                } else {
//                    return false;
//                }
                return true;
            }
        });
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
//            getDataFromServer();
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(tv_name.getText() + musicName);
            textView4 = (TextView) view.findViewById(R.id.textView4);
            tv_name.setSelected(true);
            mainActivity = (MainActivity)getActivity();

            mainActivity.pauseMediaFromMainActivity();

            setBackEvent();

            ListView expandableListView = (ListView) view.findViewById(R.id.listView);

            final Resources res =getResources();
            getDateLiveFromServer();
            adapter = new LiveAdapter(getActivity(), res, itemLiveList);
            expandableListView.setAdapter(adapter);
            expandableListView.setDivider(null);
            list_type=itemLiveList.get(0).getType();
            String type = itemLiveList.get(0).getType()=="0" ? "Rerun" : "Live";
            textView4.setText(type + " : ");
            tv_name.setText(itemLiveList.get(0).getTitle());
            String[] s = itemLiveList.get(0).getUrl().split(mainActivity.getCutURLYoutube());
            if(s.length>0){
                youtubeName = s[s.length - 1];
            }
            expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    live = itemLiveList.get(position);

                    Toast.makeText(getActivity().getApplicationContext(), "Play " + live.getTitle(), Toast.LENGTH_LONG).show();


                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                    list_type = live.getType();

                    if(list_type!=null && list_type.equals("0")) {
                        if(youTubePlayerFragment == null || YPlayer == null){
                            youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
                            transaction.remove(streamFragment);
                            streamFragment = null;
                            youTubePlayerFragment.initialize(YoutubeDeveloperKey, new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                                    if (!b) {
                                        YPlayer = youTubePlayer;
                                        YPlayer.setFullscreen(false);
                                        String url = live.getUrl();
                                        String[] strings = url.split(mainActivity.getCutURLYoutube());
                                        if(strings.length > 0) {
                                            YPlayer.loadVideo(strings[strings.length - 1]);
                                            String type_name = live.getType()=="0" ? "Rerun" : "Live";
                                            textView4.setText(type_name + " : ");
                                            tv_name.setText(live.getTitle());
                                            if(!YPlayer.isPlaying()){
                                                YPlayer.play();
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                                    // TODO Auto-generated method stub
                                }
                            });
                        }else{
                            String url = live.getUrl();
                            String[] strings = url.split(mainActivity.getCutURLYoutube());
                            if(strings.length > 0) {
                                YPlayer.loadVideo(strings[strings.length - 1]);
                                String type_name = live.getType()=="0" ? "Rerun" : "Live";
                                textView4.setText(type_name + " : ");
                                tv_name.setText(live.getTitle());
                                if(!YPlayer.isPlaying()){
                                    YPlayer.play();
                                }
                            }
                        }
                    }else{
                        if(streamFragment == null) {
                            streamFragment = new StreamFragment();
                            streamFragment.setUrl(live.getUrl());
                            if (streamFragment != null)
                                transaction.add(R.id.youtube_fragment, streamFragment).commit();
                            transaction.remove(youTubePlayerFragment);
                            youTubePlayerFragment = null;
                        }
                    }
                }
            });
            new InitialYoutube().execute();
        }catch (Exception e){

        }

        return view;
    }

    private class InitialYoutube extends AsyncTask{

        private ProgressDialog progressDialog;

        private InitialYoutube() {
            progressDialog = new ProgressDialog(getActivity());
        }

        protected void onPreExecute() {
            this.progressDialog.setMessage("Please Wait");
            this.progressDialog.show();
            try {
                wait(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("Youtube","On Post Exe");

            if(this.progressDialog.isShowing()){
                this.progressDialog.dismiss();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if(list_type!=null && list_type.equals("0")) {
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
                return YPlayer;
            }else{
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                streamFragment = new StreamFragment();
                streamFragment.setUrl(youtubeName);
                transaction.add(R.id.youtube_fragment, streamFragment).commit();
                youTubePlayerFragment = null;
                YPlayer = null;
                return view;
            }
        }
    }
}
