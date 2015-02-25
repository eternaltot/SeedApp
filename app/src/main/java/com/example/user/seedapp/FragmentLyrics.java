package com.example.user.seedapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by LacNoito on 2/15/2015.
 */
public class FragmentLyrics extends Fragment {
    private static View view;

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

//    public void testServer(){
//        HttpClient httpclient = new DefaultHttpClient();
//    }


    public void testServer(){
        try {

            URL url = new URL("http://192.168.43.174/testServer/");
//            URL url = new URL("http://hmkcode.appspot.com/jsonservlet");
            URLConnection conn = url.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("POST");
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);

            JSONObject jsonObject = new JSONObject(parsedString);

            JSONArray jsonArray = (JSONArray) jsonObject.get("dj_info");

            for(int x= 0 ; x < jsonArray.length() ; ++x){
                JSONObject object = jsonArray.getJSONObject(x);

                Log.d("system", object.getString("name"));
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
            view = inflater.inflate(R.layout.fragment_lyrics, container, false);

            testServer();

//            Socket socket = new Socket("192.168.1.2", 444);
//
//            Scanner in = new Scanner(socket.getInputStream());
//            PrintWriter out = new PrintWriter(socket.getOutputStream());
//            out.println("Lack");
//            out.flush();
//            String message = in.nextLine();
//            socket.close();

//            Log.d("system", message);

        }catch (Exception e){

        }

        return view;
    }
}
