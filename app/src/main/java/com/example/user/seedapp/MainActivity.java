package com.example.user.seedapp;

import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends FragmentActivity {

    FragmentTransaction transaction;
    JSONArray dj_info_array;


    public JSONArray getDJInfoArray(){
        return dj_info_array;
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

        FragmentMain fragmentMain = new FragmentMain();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragmentMain);
        transaction.commit();
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
