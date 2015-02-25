package com.example.user.seedapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.seedapp.com.add.model.Music;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by LacNoito on 2/15/2015.
 */
public class FragmentLyrics extends Fragment {
    private static View view;
    private Music music;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_lyrics, container, false);

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView author = (TextView) view.findViewById(R.id.author);
            TextView tempo = (TextView) view.findViewById(R.id.tempo);
            TextView compose = (TextView) view.findViewById(R.id.compose);
            TextView lyrics = (TextView) view.findViewById(R.id.lyrics);

            getDataFromServer();

            if(music != null){
                if(music.getName() != null)
                    name.setText(name.getText() + music.getName());
                else
                    name.setText(name.getText() + "-");
                if(music.getAuthor() != null)
                    author.setText(author.getText() + music.getAuthor());
                else
                    author.setText(author.getText() + "-");
                if(music.getTempo() != null)
                    tempo.setText(tempo.getText() + music.getTempo());
                else
                    tempo.setText(tempo.getText() + "-");
                if(music.getCompose() != null)
                    compose.setText(compose.getText() + music.getCompose());
                else
                    compose.setText(compose.getText() + "-");
                if(music.getLyrics() != null)
                    lyrics.setText(lyrics.getText() + music.getLyrics());
                else
                    lyrics.setText(lyrics.getText() + "-");
            }

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

            String parsedString = "{\"Music\":{\"name\":\"คิดถึง - bodyslam\",\"author\":\"ขจรเดช พรมรักษา, โป โปษยะกุล\",\"tempo\":\"อาทิวราห์ คงมาลัย\",\"compose\":\"bodyslam, พูนศักดิ์ จตุระบุล\",\"lyrics\":\"มองดูเส้นทางที่ฉันเดินอยู่ จะมีใครรู้ใครเข้าใจ เรื่องราว ร้อยพัน ที่ฉันผ่านพบช่างมากมาย\n" +
                    "ทางเดินสุดไกลแค่ไหนยังฝ่าฟัน เผชิญกับความเปลี่ยนผันเท่าไร หัวใจแสนเหนื่อย\n" +
                    "ยังคงเดินทางไปกับความจริง เพื่อสิ่งสวยงามที่อยู่ในฝัน ในหัวใจยังเป็นอยู่ทุกวัน\n" +
                    "บางคราวรอยยิ้มวันเก่า ก็พาความเหงาให้จากไป แม้ใจรู้ ดี ไม่มีวันกลับคืนมา\n" +
                    "ท่ามกลางละอองความหลังที่ปกคลุม ก็มีบางครั้งที่ยังเสียใจ แค่เพียงนึกถึงน้ำตาก็ไหล\n" +
                    "ยังคงเดินทางไปกับความจริง เพื่อสิ่งสวยงามที่อยู่ในฝัน แต่หัวใจคือเธออยู่ทุกวัน\n" +
                    "ไม่เคยเลือนลางไปจากความจริง ไม่เคยลบเลือนไปจากความฝัน อยู่หนใด เธอยังอยู่กับฉัน\n" +
                    "มันยังคิดถึงเพียงเธอ มันยังคิดถึงแค่เธอ มันยังคิดถึงเธอ\n" +
                    "เพลงนี้ คือเสียงของความว่างเปล่า คือเสียงของคนที่ปวดร้าว คือเสียงของใจที่เฝ้ารอ\n" +
                    "ยังคงเดินทางไปกับความจริง เพื่อสิ่งสวยงามที่อยู่ในฝัน แต่หัวใจคือเธออยู่ในนั้น\n" +
                    "ไม่เคยเลือนลางไปจากความจริง ไม่เคยลบเลือนไปจากความฝัน อยู่หนใด เธอยังอยู่กับฉัน\n" +
                    "มันยังคิดถึงเพียงเธอ ยังคิดถึงแค่เธอ มันยังคิดถึงเพียงเธอ ในหัวใจคือเธออยู่ทุกวัน\n" +
                    "มันยังคิดถึงเพียง เธอ ยังคิดถึง คิดถึงเธอ\"}}";
            JSONObject jsonObject = new JSONObject(parsedString);

            Gson gson = new Gson();
            music = gson.fromJson(jsonObject.getString("Music"), Music.class);


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
