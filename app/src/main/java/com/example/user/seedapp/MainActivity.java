package com.example.user.seedapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.user.seedapp.com.add.model.DJInfo;
import com.example.user.seedapp.com.add.model.ListPageItem;
import com.example.user.seedapp.com.add.model.PlayAndNext;
import com.example.user.seedapp.com.add.view.AutoScrollViewPager;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    FragmentTransaction transaction;
    JSONArray dj_info_array;
    Animation animationSlideInLeft, animationSlideOutRight;
    private String url = "http://203.147.16.93:8000/seedcave.mp3";
    private Bitmap image;
    private ArrayList<String> arrBanner = new ArrayList<String>();
    private static String banner = "http://api.seedmcot.com/api/top-banners";
    public static String list_privilege = "http://api.seedmcot.com/api/privileges";
    public static String path_Image_Topbanner = "http://api.seedmcot.com/backoffice/uploads/topbanner/2x_";
    public static String path_Image_Privilege = "http://api.seedmcot.com/backoffice/uploads/privilege/small/2x_";
    public static String path_Image_Privilege_Child = "http://api.seedmcot.com/backoffice/uploads/privilege/big/2x_";
    public static String path_Image_dj = "http://api.seedmcot.com/backoffice/uploads/dj/2x_";
    public static String list_dj = "http://api.seedmcot.com/api/dj-schedules?expand=dj";
    public static String path_nowPlaying = "http://api.seedmcot.com/api/now-playings?fields=actual_date_time,event_type,link_title&expand=linkTitle,songTitle,songCover,linkCover,linkUrl,nowLyric,nowMv,nowAuthor,nowAuthor2,nowAuthor3";
    public static String path_radio = "http://api.seedmcot.com/api/radio-urls";
    public static String list_song_details = "http://api.seedmcot.com/api/song-details";
    public static String list_lives = "http://api.seedmcot.com/api/lives";
    private ImageView imageView;
    private static int SPLASH_TIMEOUT = 15000;
//    private List<ListPageItem> listPageItems = new ArrayList<ListPageItem>();
    private static Boolean flagGetListStatus = Boolean.FALSE;
    private static FragmentMain fragmentMain;
    private PlayAndNext currentPlay;
    private PlayAndNext nextPlay;
    private Gson gson = new Gson();
    private String cutURLYoutube = "v=";

    private List<DJInfo> djInfos = new ArrayList<DJInfo>();

    private JSONArray jsonBanner;
    private JSONArray jsonPrivillege;

    public String getCutURLYoutube() {
        return cutURLYoutube;
    }

    public Gson getGson() {
        return gson;
    }

    public String getURL(){
        return url;
    }

    public FragmentMain getFragmentMain(){ return fragmentMain; };

    public void pauseMediaFromMainActivity(){
        if(fragmentMain != null){
            fragmentMain.pauseMedia();
        }
    }

    public JSONArray getDJInfoArray(){
        return dj_info_array;
    }

    public PlayAndNext getNextPlay() {
        return nextPlay;
    }

    public PlayAndNext getCurrentPlay() {
        return currentPlay;
    }

    public List<DJInfo> getDJInfos(){
        return djInfos;
    }

    public Context returnBaseContext(){
        return getBaseContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        new GetDataNowPlayingTask().execute();
//        getDataNowPlayingFromServer();
        getDataListMusic();
        getDataDJListMusicFromServer();
        getDataBanner();

//        imageView = (ImageView) findViewById(R.id.imageView4);
        BannerAdapter adapter = null;
        try {
            adapter = new BannerAdapter(getSupportFragmentManager(),jsonBanner );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AutoScrollViewPager pager = (AutoScrollViewPager)findViewById(R.id.pagebanner);
        pager.setAdapter(adapter);
        pager.startAutoScroll();
        pager.setInterval(1000);
        pager.setCycle(true);
        pager.setAutoScrollDurationFactor(200);

        fragmentMain = new FragmentMain();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragmentMain);
        transaction.commit();

        final ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
        final ImageButton btnSeed = (ImageButton) findViewById(R.id.btnSeed);
        final ImageButton btnStream = (ImageButton) findViewById(R.id.btnStream);
        btnHome.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHome.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                btnSeed.setImageResource(R.drawable.icon_seed_app_logo_75px_white);
                btnStream.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                setFragment(fragmentMain);
            }
        });

        btnSeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSeed.setImageResource(R.drawable.icon_seed_app_logo_75px_red);
                btnHome.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                btnStream.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                FragmentPrivilege fragmentPrivilege = new FragmentPrivilege();
                setFragment(fragmentPrivilege);

            }
        });

        btnStream.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        btnStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStream.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                btnHome.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                btnSeed.setImageResource(R.drawable.icon_seed_app_logo_75px_white);
                FragmentLive fragmentLive = new FragmentLive();
                setFragment(fragmentLive);
            }
        });
    }

    public void setFragment(Fragment fragment){
        try {
            transaction = getSupportFragmentManager().beginTransaction();

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (f.getClass() != fragment.getClass()) {
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } catch (Exception ex){

        }
//        transaction.add(R.id.fragment_container, fragment).commit();
    }


    public void setFragmentNoBack(Fragment fragment){
        try {
            transaction = getSupportFragmentManager().beginTransaction();

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if(f.getClass() != fragment.getClass()) {
                if (fragment instanceof FragmentMain) {
                    transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down);
                } else {
                    transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                }
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }catch (Exception ex){

        }

//        transaction.add(R.id.fragment_container, fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDataListMusic(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(path_radio);
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = convertinputStreamToString(instream);
                Log.d("system", "Sucess!!!!");
                Log.d("system", result);

                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    url = (String) jsonObject.get("url");
                    Log.d("system", "url :: " + url);
                    Log.d("system", "url :: " + url);
                }

            } catch (Exception e) {
                Log.e("system", "Error!!!!");
                Log.e("system", e.getMessage());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataBanner(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(banner);
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = convertinputStreamToString(instream);
                Log.d("system", "Sucess!!!!");
                Log.d("system", result);

                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null){
                    jsonBanner = jsonArray;
                    Log.d("system", "banner :: " + jsonBanner.toString());
                }

            } catch (Exception e) {
                Log.e("system", "Error!!!!");
                Log.e("system", e.getMessage());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getDataPrivillege(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(list_privilege);
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = convertinputStreamToString(instream);
                Log.d("system", "Sucess!!!!");
                Log.d("system", result);

                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null){
                    jsonPrivillege = jsonArray;
                    Log.d("system", "listPrivillege :: " + jsonPrivillege.toString());
                }

            } catch (Exception e) {
                Log.e("system", "Error!!!!");
                Log.e("system", e.getMessage());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonPrivillege;
    }

    public void getDataDJListMusicFromServer(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(list_dj);
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = convertinputStreamToString(instream);
                Log.d("system", "Sucess!!!!");
                Log.d("system", result);

                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null){
                    for(int x= 0 ; x < jsonArray.length() ; ++x){
                        JSONObject object = jsonArray.getJSONObject(x);

                        DJInfo djInfo = new DJInfo();
                        djInfo.setDjId((Integer) object.get("dj_id"));
                        djInfo.setStartTime(object.getString("start_time"));
                        djInfo.setStopTime(object.getString("stop_time"));

                        JSONObject djJSON = (JSONObject) object.get("dj");
                        if(djJSON != null) {
                            djInfo.setName(djJSON.getString("name"));
                            djInfo.setImage(path_Image_dj + djJSON.getString("image"));
                            URL newurl = new URL(djInfo.getImage());
                            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                            djInfo.setBitmap(mIcon_val);
                        }
                        djInfo.setJSONObject(object);
                        djInfos.add(djInfo);
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


    public void getDataNowPlayingFromServer(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(path_nowPlaying);
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            String result = convertinputStreamToString(instream);
            Log.d("system", "Sucess!!!!");
            Log.d("system", result);

            JSONArray jsonArray = new JSONArray(result);
            if(jsonArray != null){
                for(int x= 0 ; x < jsonArray.length() ; ++x){
                    JSONObject object = jsonArray.getJSONObject(x);

                    Log.e("system", "object.toString() :: " + object.toString());

                    if(x == 0) {
                        currentPlay = gson.fromJson(object.toString(), PlayAndNext.class);
                    }
                    else if(x == 1) {
                        nextPlay = gson.fromJson(object.toString(), PlayAndNext.class);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("system", "Error getDataNowPlayingFromServer!!!!");
            Log.e("system", e.getMessage());
        }
    }

    public void getDataList(){
        if(flagGetListStatus){
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    getDataNowPlayingFromServer();
                    new GetDataNowPlayingTask().execute();
                }
            }, SPLASH_TIMEOUT);
        }else{
            getDataNowPlayingFromServer();
            flagGetListStatus = Boolean.TRUE;
            new GetDataNowPlayingTask().execute();
        }
    }

    class GetDataNowPlayingTask extends AsyncTask<String, String, String> {

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
            runOnUiThread(new Runnable() {
                public void run() {
                    getDataList();
                }
            });
        }
    }
}
