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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.user.seedapp.com.add.model.DJInfo;
import com.example.user.seedapp.com.add.model.ListPageItem;
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
    private static String path_Image_Topbanner = "http://api.seedmcot.com/backoffice/uploads/topbanner/2x_";
    private ImageView imageView;
    private static int SPLASH_TIMEOUT = 3000;
//    private List<ListPageItem> listPageItems = new ArrayList<ListPageItem>();
    private static Boolean flagGetListStatus = Boolean.FALSE;
    private static FragmentMain fragmentMain;

    private List<DJInfo> djInfos = new ArrayList<DJInfo>();

    private JSONArray jsonBanner;

//    public List<ListPageItem> getListPageItems(){return listPageItems;}

    public String getURL(){
        return url;
    }

    public void pauseMediaFromMainActivity(){
        if(fragmentMain != null){
            fragmentMain.pauseMedia();
        }
    }

    public JSONArray getDJInfoArray(){
        return dj_info_array;
    }

    public List<DJInfo> getDJInfos(){
        return djInfos;
    }

    public Context returnBaseContext(){
        return getBaseContext();
    }

    public void setDjInfos() throws Exception {
        for(int x= 0 ; x < dj_info_array.length() ; ++x){
            JSONObject jsonObject = dj_info_array.getJSONObject(x);
            DJInfo djInfo = new DJInfo();
            djInfo.setJSONObject(jsonObject);
            URL newurl = new URL(jsonObject.getString("image"));
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            djInfo.setBitmap(mIcon_val);
            djInfos.add(djInfo);
        }
    }
    public void setBanner() throws Exception {
        for(int x= 0 ; x < jsonBanner.length() ; ++x){
            Log.d("system",jsonBanner.getJSONObject(x).get("image").toString());
            arrBanner.add(jsonBanner.getJSONObject(x).get("image").toString());
        }
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

        getDateListMusic();
        getDataFromServer();
        getDataBanner();
        try {
            setBanner();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView = (ImageView) findViewById(R.id.imageView4);
        String urbaner = path_Image_Topbanner + arrBanner.get(0);
        try {
            URL urlBanner = new URL(urbaner);
            Bitmap bmp = BitmapFactory.decodeStream(urlBanner.openConnection().getInputStream());
            Log.d("system" , "Set Image " + bmp.toString() );
            imageView.setImageBitmap(bmp);
        } catch (IOException e) {
            Log.e("system",e.getMessage());
        }

        animationSlideInLeft = AnimationUtils.loadAnimation(this,
                R.anim.slide_left);
        animationSlideOutRight = AnimationUtils.loadAnimation(this,
                R.anim.slide_right);
        animationSlideInLeft.setDuration(15000);
        animationSlideOutRight.setDuration(15000);
        animationSlideInLeft.setAnimationListener(animationSlideInLeftListener);
        animationSlideOutRight.setAnimationListener(animationSlideOutRightListener);

  
        imageView.startAnimation(animationSlideInLeft);




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

//        new GetDataListTask().execute();
    }

    public void setFragment(Fragment fragment){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    public void getDateListMusic(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://api.seedmcot.com/api/radio-urls");
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

            String parsedString = "{\"dj_info\":[{\"name\":\"TOT\",\"image\":\"http:\\/\\/i.ytimg.com\\/vi\\/SehciaH-pBo\\/maxresdefault.jpg\",\"time_online\":\"07.00 - 09.00\"},{\"name\":\"Bank\",\"image\":\"http:\\/\\/image.dek-d.com\\/24\\/2172565\\/105557011\",\"time_online\":\"09.00 - 12.00\"},{\"name\":\"Top\",\"image\":\"http:\\/\\/inkjet.weloveshopping.com\\/shop\\/client\\/000032\\/inkjet\\/webboard\\/N2975877.jpg\",\"time_online\":\"13.00 - 15.00\"},{\"name\":\"Poo\",\"image\":\"http:\\/\\/www.dmc.tv\\/images\\/reli-550-2.jpg\",\"time_online\":\"16.00 - 20.00\"},{\"name\":\"WoW!!\",\"image\":\"http:\\/\\/static.playinter.com\\/data\\/attachment\\/image\\/2012\\/07\\/19\\/1602-r-1342643168501.jpg\",\"time_online\":\"20.00 - 22.00\"}],\"a\":\"111\"}";
            JSONObject jsonObject = new JSONObject(parsedString);
            dj_info_array = (JSONArray) jsonObject.get("dj_info");
            if(dj_info_array != null){
                setDjInfos();
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

//    public void getDateListFromServer(){
//        try {
//
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpGet request = new HttpGet("http://api.seedmcot.com/api/song-details");
//            request.setHeader("Content-Type", "text/xml");
//            HttpResponse response;
//            try {
//                response = httpClient.execute(request);
//                HttpEntity entity = response.getEntity();
//                InputStream instream = entity.getContent();
//                String result = convertinputStreamToString(instream);
//                Log.e("system", "Sucess!!!!");
//                Log.e("system", "DATA List" + result);
//
//                JSONArray jsonArray = new JSONArray(result);
//                for(int x= 0 ; x < jsonArray.length() ; ++x){
//                    JSONObject object = jsonArray.getJSONObject(x);
//                    Gson gson = new Gson();
//                    ListPageItem listPageItem = gson.fromJson(object.toString(), ListPageItem.class);
//                    listPageItems.add(listPageItem);
//                }
//
//                if(fragmentMain != null){
//                    fragmentMain.updateListViewFragmentListPageFromFragmentMain();
//                }
//            } catch (Exception e) {
//                Log.e("system", "Error!!!!");
//                Log.e("system", e.getMessage());
//            }
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    public void getDataList(){
//        if(flagGetListStatus){
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    getDateListFromServer();
//                    new GetDataListTask().execute();
//                }
//            }, SPLASH_TIMEOUT);
//        }else{
//            getDateListFromServer();
//            flagGetListStatus = Boolean.TRUE;
//            new GetDataListTask().execute();
//        }
//    }

//    class GetDataListTask extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... args) {
//
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        @Override
//        protected void onPostExecute(String file_url) {
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    getDataList();
//                }
//            });
//        }
//    }
    Animation.AnimationListener animationSlideInLeftListener
            = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            imageView.startAnimation(animationSlideOutRight);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    Animation.AnimationListener animationSlideOutRightListener
            = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {

            imageView.startAnimation(animationSlideInLeft);
        }

        public void onAnimationRepeat(Animation animation) {

        }

        public void onAnimationStart(Animation animation) {

        }
    };


}
