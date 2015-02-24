package com.example.user.seedapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.user.seedapp.com.add.model.DJInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    FragmentTransaction transaction;
    JSONArray dj_info_array;
    private List<DJInfo> djInfos = new ArrayList<DJInfo>();

    public JSONArray getDJInfoArray(){
        return dj_info_array;
    }

    public List<DJInfo> getDJInfos(){
        return djInfos;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getDataFromServer();

        final FragmentMain fragmentMain = new FragmentMain();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragmentMain);
        transaction.commit();

        final ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
        final ImageButton btnSeed = (ImageButton) findViewById(R.id.btnSeed);
        final ImageButton btnStream = (ImageButton) findViewById(R.id.btnStream);
        btnHome.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
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
                FragmentYouTube fragmentYouTube = new FragmentYouTube();
                setFragment(fragmentYouTube);
            }
        });
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

            String parsedString = "{\"dj_info\":[{\"name\":\"Lack:1\",\"image\":\"http:\\/\\/i.ytimg.com\\/vi\\/SehciaH-pBo\\/maxresdefault.jpg\",\"time_online\":\"07.00 - 09.00\"},{\"name\":\"Lack:2\",\"image\":\"http:\\/\\/www.pattanakit.net\\/images\\/1202426018\\/boss_beating_fist_on__a_ha.gif\",\"time_online\":\"09.00 - 12.00\"},{\"name\":\"Lack:3\",\"image\":\"http:\\/\\/inkjet.weloveshopping.com\\/shop\\/client\\/000032\\/inkjet\\/webboard\\/N2975877.jpg\",\"time_online\":\"13.00 - 15.00\"},{\"name\":\"Lack:4\",\"image\":\"http:\\/\\/www.dmc.tv\\/images\\/reli-550-2.jpg\",\"time_online\":\"16.00 - 20.00\"},{\"name\":\"Lack:5\",\"image\":\"http:\\/\\/static.playinter.com\\/data\\/attachment\\/image\\/2012\\/07\\/19\\/1602-r-1342643168501.jpg\",\"time_online\":\"20.00 - 22.00\"}],\"a\":\"111\"}";
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
}
