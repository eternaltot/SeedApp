package com.seedmcot.seedcave;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.seedmcot.seedcave.add.model.ListPageItem;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LacNoito on 3/8/2015.
 */
public class FragmentListPage extends Fragment {

    private static View view;
    private ListPageAdapter adapter;
    private static ListView expandableListView;
    private List<ListPageItem> listPageItems = new ArrayList<ListPageItem>();
    private TextView number_tv;
    private TextView nowPlaying;
    private TextView name;
    private MainActivity mainActivity;
    private ImageView back_bt;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_list_page, container, false);

            expandableListView = (ListView) view.findViewById(R.id.listView);
            number_tv = (TextView) view.findViewById(R.id.number_tv);
            nowPlaying = (TextView) view.findViewById(R.id.now_playing);
            name = (TextView) view.findViewById(R.id.name);
            back_bt = (ImageView) view.findViewById(R.id.back_bt);
            back_bt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mainActivity.setFragmentNoBack(mainActivity.getFragmentMain());
                }
            });
            name.setSelected(true);

            mainActivity = (MainActivity) getActivity();
            mainActivity.setTypeFace(nowPlaying);
            mainActivity.setTypeFace(number_tv);
            mainActivity.setTypeFaceOther(name);

            if(mainActivity.getCurrentPlay()!=null && mainActivity.getCurrentPlay().getEvent_type().equals("song")){
                name.setText(mainActivity.getCurrentPlay().getSongTitle()!=null ? mainActivity.getCurrentPlay().getSongTitle():"Seed 97.5 FM");
                name.setText(name.getText() + (mainActivity.getCurrentPlay().getArtistName() != null && mainActivity.getCurrentPlay().getArtistName() != "" ? "-" + mainActivity.getCurrentPlay().getArtistName() : ""));
            }else if(mainActivity.getCurrentPlay().getEvent_type().equals("spot")){
                name.setText(mainActivity.getCurrentPlay().getLink_title()!=null ? mainActivity.getCurrentPlay().getLink_title(): mainActivity.getCurrentPlay().getLinkTitle() != null ? mainActivity.getCurrentPlay().getLinkTitle() : "Seed 97.5 FM");
            }

            getDateListFromServer();

            number_tv.setText("SEED LIST");

            updateListView();


        }catch (InflateException e){

        }

        setBackEvent();

        return view;
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
                return view.onKeyDown(keyCode, event);
            }
        });
    }

    public void updateListView(){
        if(expandableListView != null) {
            adapter = new ListPageAdapter(getActivity(), listPageItems);
            expandableListView.setAdapter(adapter);
            expandableListView.setDivider(null);
        }
    }

    public void getDateListFromServer(){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(mainActivity.list_song_details+ "&t=" + new Date().getTime());
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = ((MainActivity) getActivity()).convertinputStreamToString(instream);

                JSONArray jsonArray = new JSONArray(result);
                for(int x= 0 ; x < jsonArray.length() ; ++x){
                    JSONObject object = jsonArray.getJSONObject(x);
                    Gson gson = new Gson();
                    ListPageItem listPageItem = gson.fromJson(object.toString(), ListPageItem.class);
                    listPageItems.add(listPageItem);
                }
            } catch (Exception e) {
                Log.e("system", e.getMessage());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
