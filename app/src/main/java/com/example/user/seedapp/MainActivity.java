package com.example.user.seedapp;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.seedapp.com.add.model.Banner;
import com.example.user.seedapp.com.add.model.DJInfo;
import com.example.user.seedapp.com.add.model.MenuBarImageButton;
import com.example.user.seedapp.com.add.model.Music;
import com.example.user.seedapp.com.add.model.PlayAndNext;
import com.example.user.seedapp.com.add.view.AutoScrollViewPager;
import com.example.user.seedapp.com.add.view.DrawableManagerTT;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends FragmentActivity {

    FragmentTransaction transaction;
    JSONArray dj_info_array;
    Animation animationSlideInLeft, animationSlideOutRight;
    private android.app.ActionBar.Tab main,privi,live;
    private String url = "http://203.147.16.93:8000/seedcave.mp3";
    private Bitmap image;
    private ArrayList<String> arrBanner = new ArrayList<String>();
    private static String banner = "http://api.seedmcot.com/api/top-banners";
    private static String big_banner = "http://api.seedmcot.com/api/big-banners";
    public static String list_privilege = "http://api.seedmcot.com/api/privileges";
    public static String path_Image_Topbanner = "http://api.seedmcot.com/backoffice/uploads/topbanner/2x_";
    public static String path_Image_Bigbanner = "http://api.seedmcot.com/backoffice/uploads/bigbanner/1x_";
    public static String path_Image_Privilege = "http://api.seedmcot.com/backoffice/uploads/privilege/small/2x_";
    public static String path_Image_Privilege_Child = "http://api.seedmcot.com/backoffice/uploads/privilege/big/2x_";
    public static String path_Thumbnail_Youtube = "http://img.youtube.com/vi/";
    public static String path_Image_Cover_NowPlaying = "http://api.seedmcot.com/backoffice/uploads/";
    public static String path_Image_dj = "http://api.seedmcot.com/backoffice/uploads/dj/2x_";
    public static String list_dj = "http://api.seedmcot.com/api/dj-schedules?expand=dj";
    public static String path_nowPlaying = "http://api.seedmcot.com/api/now-playings?fields=actual_date_time,event_type,link_title&expand=linkTitle,songTitle,songCover,linkCover,linkUrl,nowLyric,nowMv,nowAuthor,nowAuthor2,nowAuthor3";
    public static String path_radio = "http://api.seedmcot.com/api/radio-urls";
    public static String list_song_details = "http://api.seedmcot.com/api/song-details";
    public static String list_lives = "http://api.seedmcot.com/api/lives";
    public static String list_menu = "http://api.seedmcot.com/api/tab-menus";
    public static String path_Image_Menu = "http://api.seedmcot.com/backoffice/uploads/tabmenu/3x_";
    public static String url_TEXT_TO_DJ="http://api.seedmcot.com/api/text-to-djs";
    private ImageView imageView;
    private static int SPLASH_TIMEOUT = 15000;
    private static YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyCjfgiAytO0iYrnz7EQuWarGLSSPmW_mw0";
    private static YouTubePlayerSupportFragment youTubePlayerFragment;
//    private List<ListPageItem> listPageItems = new ArrayList<ListPageItem>();
    private static Boolean flagGetListStatus = Boolean.FALSE;
    private static FragmentMain fragmentMain;
    private static FragmentYouTube fragmentYouTube;
    private static FragmentLyrics fragmentLyrics;
    private static FragmentListPage fragmentListPage;
    private FragmentPrivilege fragmentPrivilege = new FragmentPrivilege();
    private FragmentLive fragmentLive = new FragmentLive();
    private PlayAndNext currentPlay;
    private PlayAndNext nextPlay;
    private Gson gson = new Gson();
    private String cutURLYoutube = "v=";
    public static DrawableManagerTT drawableManagerTT;
    private Map<Object, Object> drawableTypeRequestLive = new HashMap<>();
    private ImageButton btnHome,btnSeed,btnStream;
    private Boolean doubleBackToExitPressedOnce = false;
    private static List<MenuBarImageButton> menuBarList = new ArrayList<>();
    private AudioManager audioManager;
    private ImageButton bt_mute;
    private Boolean seekMute = Boolean.FALSE;
    private int seekVal = -1;
    private SeekBar seekBar;

    public int getSeekVal() {
        return seekVal;
    }

    public void setSeekVal(int seekVal) {
        this.seekVal = seekVal;
    }

    public ImageButton getBt_mute() {
        return bt_mute;
    }

    public Boolean getSeekMute() {
        return seekMute;
    }

    public void setSeekMute(Boolean seekMute) {
        this.seekMute = seekMute;
    }

    public void setBt_mute(ImageButton bt_mute) {
        this.bt_mute = bt_mute;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public Map<Object, Object> getDrawableTypeRequestLive() {
        return drawableTypeRequestLive;
    }

    private List<DJInfo> djInfos = new ArrayList<DJInfo>();

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public static YouTubePlayer getYPlayer() {
        return YPlayer;
    }

    public static YouTubePlayerSupportFragment getYouTubePlayerFragment() {
        return youTubePlayerFragment;
    }

    public static void setYouTubePlayerFragment(YouTubePlayerSupportFragment youTubePlayerFragment) {
        MainActivity.youTubePlayerFragment = youTubePlayerFragment;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public static void setYPlayer(YouTubePlayer YPlayer) {
        MainActivity.YPlayer = YPlayer;
    }

    private JSONArray jsonBanner,jsonBigBanner;
    private JSONArray jsonPrivillege;
    private JSONArray jsonMenu;
    private Typeface typeface;

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

    public void setDjInfos() throws Exception {
        djInfos.clear();

        for(int x= 0 ; x < dj_info_array.length() ; ++x){
            JSONObject jsonObject = dj_info_array.getJSONObject(x);
            DJInfo djInfo = new DJInfo();
            djInfo.setJSONObject(jsonObject);
            URL newurl = new URL(jsonObject.getString("image"));
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            djInfo.setBitmap(mIcon_val);
            djInfos.add(djInfo);

            FragmentDJIndoPage fragmentDJIndoPage = new FragmentDJIndoPage();
            fragmentDJIndoPage.setObject(djInfo);
        }
    }
//    public void setBanner() throws Exception {
//        for(int x= 0 ; x < jsonBanner.length() ; ++x){
//            Log.d("system",jsonBanner.getJSONObject(x).get("image").toString());
//            arrBanner.add(path_Image_Topbanner + jsonBanner.getJSONObject(x).get("image").toString());
//        }
//    }


    @Override
    protected void onResume() {
        Log.d("system", "This onResume!!");
        for(MenuBarImageButton menuBarImageButton : menuBarList){
            menuBarImageButton.getImageButton().setImageBitmap(menuBarImageButton.getBitmap());
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ReplaceFont.replaceDefaultFont(this, "DEFAULT", "alpine_typeface/AlpineTypeface/Cleanlight.ttf");

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        drawableManagerTT = new DrawableManagerTT();

        new GetDataPrivilegeTask().execute();
        new GetDataNowPlayingTask().execute();
//        getDataNowPlayingFromServer();
        getDataListMusic();
        getDataDJListMusicFromServer();
        getDataBanner();
        getDataBigBanner();
        getDataNowPlayingFromServer();
        getDataMenu();

//        imageView = (ImageView) findViewById(R.id.imageView4);
        BannerAdapter adapter = null;
        try {
            adapter = new BannerAdapter(getSupportFragmentManager(),jsonBanner);
        } catch (IOException e) {
            Log.e("system",e.getMessage());
        } catch (JSONException e) {
            Log.e("system",e.getMessage());
        }
        AutoScrollViewPager pager = (AutoScrollViewPager)findViewById(R.id.pagebanner);
        pager.setAdapter(adapter);
        pager.startAutoScroll();
        pager.setInterval(1000);
        pager.setCycle(true);
        pager.setAutoScrollDurationFactor(200);
        Log.d("system", "Set Top Banner Success");

        fragmentMain = new FragmentMain();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragmentMain);
        transaction.commit();
        setNowPlaying();
        fragmentListPage = new FragmentListPage();
        fragmentYouTube = new FragmentYouTube();
        fragmentLyrics = new FragmentLyrics();

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnSeed = (ImageButton) findViewById(R.id.btnSeed);
        btnStream = (ImageButton) findViewById(R.id.btnStream);
//        btnHome.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnHome.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                btnHome.setImageResource(R.drawable.headphone_button_active);
                btnSeed.setImageResource(R.drawable.headseed_button);
                btnStream.setImageResource(R.drawable.eye_button);
//                btnStream.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                if(fragmentMain.isVisible() || fragmentListPage.isVisible() || fragmentLyrics.isVisible() || fragmentYouTube.isVisible()){

                }else{
                    setFragment(fragmentMain);
                }

            }
        });

        btnSeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnSeed.setImageResource(R.drawable.menu2_on);
//                btnHome.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//                btnStream.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                btnHome.setImageResource(R.drawable.headphone_button);
                btnSeed.setImageResource(R.drawable.headseed_button_active);
                btnStream.setImageResource(R.drawable.eye_button);
                FragmentPrivilege fragmentPrivilege = new FragmentPrivilege();
                setFragment(fragmentPrivilege);

            }
        });

//        btnStream.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        btnStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnStream.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
//                btnHome.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//                btnSeed.setImageResource(R.drawable.menu2);
                btnHome.setImageResource(R.drawable.headphone_button);
                btnSeed.setImageResource(R.drawable.headseed_button);
                btnStream.setImageResource(R.drawable.eye_button_active);
                FragmentLive fragmentLive = new FragmentLive();
                fragmentLive.setYouTubePlayerFragment(YPlayer);
                setFragment(fragmentLive);
            }
        });
        final Dialog dialog_banner = new Dialog(MainActivity.this);
        dialog_banner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.alert_banner,null,false);
        ImageView imgCloseBtn = (ImageView) convertView.findViewById(R.id.close_dialog);
        ImageView imgBigBanner = (ImageView)convertView.findViewById(R.id.imgBigBanner);
        Random rand = new Random();
        final int n = rand.nextInt(jsonBigBanner.length());
        Banner bann = new Banner();
        Log.d("system","Random Number :: " + n);
        try {
            Glide.with(MainActivity.this).load(path_Image_Bigbanner + (String) jsonBigBanner.getJSONObject(n).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgBigBanner);
            imgBigBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(),WebviewActivity.class);
                    try {
                        intent.putExtra("URL", jsonBigBanner.getJSONObject(n).get("url_web").toString());
                    } catch (JSONException e) {
                        intent.putExtra("URL", "");
                        e.printStackTrace();
                    }
//                    Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, R.anim.slide_out_up).toBundle();
//                    startActivity(intent, bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imgCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog_banner != null && dialog_banner.isShowing()) {
                    dialog_banner.dismiss();
                }
            }
        });
        dialog_banner.setContentView(convertView);
        dialog_banner.show();
        fragmentMain.sendEventClickPlay();
        setMenu();









//        new GetDataListTask().execute();
    }

    public void setFragment(Fragment fragment){
        try {
            transaction = getSupportFragmentManager().beginTransaction();

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (f.getClass() != fragment.getClass()) {
                if(fragment.getClass() == FragmentMain.class && (f.getClass() == FragmentLyrics.class || f.getClass() == FragmentYouTube.class || f.getClass() == FragmentListPage.class)){
                    return;
                }
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
            Log.e("Error", ex.getMessage());
        }

//        transaction.add(R.id.fragment_container, fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            finish();
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);

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
            Log.e("system", "Error!!!!");
            Log.e("system", e.getMessage());
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

    public void getDataBigBanner(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(big_banner);
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
                    jsonBigBanner = jsonArray;
                    Log.d("system", "big banner :: " + jsonBigBanner.toString());
                    Log.d("system", "big banner lenght :: " + jsonBigBanner.length());
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

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String s = MainActivity.path_Image_Privilege + (String) jsonArray.getJSONObject(i).get("small_image");
                        drawableManagerTT.drawableBitMapOnThread(s);
                        String image_big = MainActivity.path_Image_Privilege_Child + (String) jsonArray.getJSONObject(i).get("big_image");
                        drawableManagerTT.fetchOnThread(image_big);
                    }
                }

            } catch (Exception e) {
                Log.e("system", "getDataPrivillege");
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
                        djInfo.setStartTime(object.getString("start_time").substring(0,5));
                        djInfo.setStopTime(object.getString("stop_time").substring(0,5));

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

                    Log.d("system", "Now Playing :: " + object.toString());

                    if(x == 1) {
                        currentPlay = gson.fromJson(object.toString(), PlayAndNext.class);
                        Log.d("system" , " Current Play Object :: " + currentPlay.getSongTitle());
                    }
                    else if(x == 0) {
                        nextPlay = gson.fromJson(object.toString(), PlayAndNext.class);
                        Log.d("system" , " Next Play Object :: " + nextPlay.getSongTitle());
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
                    if(fragmentMain!=null && fragmentMain.isVisible()){
                        Log.d("system","ON Fragment Main Other Time");
                        setNowPlaying();
                        setComponentInFragmentMain();
                    }
                    if(fragmentYouTube!=null && fragmentYouTube.isVisible()){
                        String s = "Now Playing : " + (getCurrentPlay().getSongTitle()!=null ? getCurrentPlay().getSongTitle():"");
                        fragmentYouTube.setTv_name(s);
                    }
                    if(fragmentLyrics!=null && fragmentLyrics.isVisible()){
                        Music music = new Music();
                        music.setLyrics(getCurrentPlay().getNowLyric());
                        music.setName(getCurrentPlay().getSongTitle() + " - " + getCurrentPlay().getNowAuthor());
                        music.setAuthor(getCurrentPlay().getNowAuthor());
                        music.setAuthor2(getCurrentPlay().getNowAuthor2());
                        music.setAuthor3(getCurrentPlay().getNowAuthor3());
                        fragmentLyrics.setMusic(music);
                    }

                    new GetDataNowPlayingTask().execute();
                }
            }, SPLASH_TIMEOUT);
        }else{
            getDataNowPlayingFromServer();
            flagGetListStatus = Boolean.TRUE;
            if(fragmentMain!=null && fragmentMain.isVisible()){
                Log.d("system","ON Fragment Main First Time");
                setNowPlaying();
                setComponentInFragmentMain();
//                fragmentMain.updateNowPlayingAndNext(getCurrentPlay() != null && getCurrentPlay().getSongTitle() != null ? getCurrentPlay().getSongTitle() : "", getNextPlay() != null && getNextPlay().getSongTitle() != null ? getNextPlay().getSongTitle() : "");
            }

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


    class GetDataPrivilegeTask extends AsyncTask<String, String, String> {

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
                    getDataPrivillege();
                }
            });
        }
    }

    public void setNowPlaying(){
        String now="Now Playing : ";
        String next="Next Song :";
        String pathImage_Cover = "";
        String url_Link = "";
        if(getCurrentPlay()!=null && getCurrentPlay().getEvent_type().equals("song")){
            now = (getCurrentPlay().getSongTitle()!=null ? getCurrentPlay().getSongTitle():"");
            pathImage_Cover = getCurrentPlay().getSongCover() != null ? getCurrentPlay().getSongCover() : "";
        }else if(getCurrentPlay().getEvent_type().equals("link")){
            now = (getCurrentPlay().getLink_title()!=null ? getCurrentPlay().getLink_title():"");
            pathImage_Cover = getCurrentPlay().getLinkCover() != null ? getCurrentPlay().getLinkCover() : "";
            url_Link = getCurrentPlay().getLinkUrl();
        }
        if(getNextPlay()!=null && getNextPlay().getEvent_type().equals("song")){
            next = (getNextPlay().getSongTitle()!=null ? getNextPlay().getSongTitle():"");
        }else if(getNextPlay().getEvent_type().equals("link")){
            next = (getNextPlay().getLink_title()!=null ? getNextPlay().getLink_title():"");
        }
        fragmentMain.updateNowPlayingAndNext(now,next,pathImage_Cover,url_Link);
    }

    public void setComponentInFragmentMain(){
        if(getCurrentPlay()!=null && (getCurrentPlay().getNowLyric()!=null && !getCurrentPlay().getNowLyric().equals(""))){
            fragmentMain.setDisableButtonLyric(true);
        }else if(getCurrentPlay()!=null && (getCurrentPlay().getNowLyric()==null || getCurrentPlay().getNowLyric().equals(""))){
            fragmentMain.setDisableButtonLyric(false);
        }
        if(getCurrentPlay()!=null && (getCurrentPlay().getNowMv()!=null && !getCurrentPlay().getNowMv().equals(""))){
            fragmentMain.setDisableButtonMV(true);
        }else if(getCurrentPlay()!=null && (getCurrentPlay().getNowMv()==null || getCurrentPlay().getNowMv().equals(""))){
            fragmentMain.setDisableButtonMV(false);
        }
//        if(getCurrentPlay()!=null && (getCurrentPlay().getNowLyric()!=null || !getCurrentPlay().getNowLyric().equals(""))){
//            fragmentMain.setDisableButtonLyric(true);
//        }else if(getCurrentPlay()!=null && (getCurrentPlay().getNowLyric()==null || getCurrentPlay().getNowLyric().equals(""))){
//            fragmentMain.setDisableButtonLyric(false);
//        }
    }

    public void getDataMenu(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(list_menu);
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            String result = convertinputStreamToString(instream);
            Log.d("system", "Load Menu Sucess!!!!");
            Log.d("system", result);

            JSONArray jsonArray = new JSONArray(result);
            jsonMenu = jsonArray;
        } catch (Exception e) {
            Log.e("system", "Error getDataNowPlayingFromServer!!!!");
            Log.e("system", e.getMessage());
        }
    }

    public void setMenu(){
        if(jsonMenu != null){

            menuBarList.clear();

            for(int i=0;i<jsonMenu.length();i++){
                try {
                    JSONObject json = jsonMenu.getJSONObject(i);
                    String icon = json.getString("icon");
                    String select_icon = json.getString("selected_icon");
                    final String url_button = json.getString("url");

                    String pic = path_Image_Menu+icon;
                    String pic_select = path_Image_Menu + select_icon;
                    LinearLayout linearMenu = (LinearLayout) findViewById(R.id.linearMenu);

                    Bitmap bitmap = ((BitmapDrawable)btnHome.getDrawable()).getBitmap();

                    Log.d("system", "bitmap.getWidth()" + bitmap.getWidth());
                    Log.d("system", "bitmap.getHeight()" + bitmap.getHeight());

//                    for(int x=0;x<10;x++) {
                        final ImageButton imageButton = new ImageButton(this);

//                        Glide.with(this).load(pic).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageButton);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER;
                        Resources r = this.getResources();
                        int px = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                15,
                                r.getDisplayMetrics()
                        );
                        int img_px = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                50,
                                r.getDisplayMetrics()
                        );
                        params.leftMargin = px;
//                        params.rightMargin = px;
                        imageButton.setLayoutParams(params);
                        URL newurl = new URL(pic);
                        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        mIcon_val = Bitmap.createScaledBitmap(mIcon_val, img_px, img_px, true);


                        imageButton.setImageBitmap(mIcon_val);
                        imageButton.setBackground(null);
//                        imageButton.setScaleType(ImageView.ScaleType.FIT_XY);

                        URL newurl_select = new URL(pic_select);
                        Bitmap mIcon_select = BitmapFactory.decodeStream(newurl_select.openConnection().getInputStream());
                        mIcon_select = Bitmap.createScaledBitmap(mIcon_select, img_px, img_px, true);
                        final Bitmap img = mIcon_select;

                        imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                imageButton.setImageBitmap(img);
                                Intent intent = new Intent(getApplicationContext(),WebviewActivity.class);
                                intent.putExtra("URL", url_button);
//                                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, R.anim.slide_out_up).toBundle();
//                                startActivity(intent, bundle);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                            }
                        });
                        linearMenu.addView(imageButton);
                        menuBarList.add(new MenuBarImageButton(imageButton, mIcon_val));
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setTypeFace(TextView textView){
        if(textView!=null){
            textView.setTypeface(getFontTypeface());
        }
    }
    private Typeface getFontTypeface(){
        if(typeface==null){
            typeface = Typeface.createFromAsset(getAssets(),"alpine_typeface/AlpineTypeface/Cleanlight.ttf");
        }
        return typeface;
    }

    @Override // เหตุการณ์เมื่อกดปุ่ม เพิ่ม-ลด ด้านข้าง
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(f instanceof FragmentMain){
            if(seekBar != null) {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
                    seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    bt_mute.setImageResource(R.drawable.speaker);

                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    int val = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,0);
                    seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    if (val - 1 == 0) {
                        bt_mute.setImageResource(R.drawable.speaker_mute_white);
                    }
                    return true;
                }
            }
        }else {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                super.onKeyDown(keyCode, event);
                if(seekBar!=null){
                    seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                }
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                super.onKeyDown(keyCode, event);
                if(seekBar!=null){
                    seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                }
                return true;
            }

        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
