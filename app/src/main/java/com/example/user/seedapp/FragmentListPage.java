package com.example.user.seedapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.seedapp.com.add.model.ListPageItem;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
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
    private TextView name;

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
            name = (TextView) view.findViewById(R.id.name);

            getDateListFromServer();

            number_tv.setText("Last " + listPageItems.size() + " songs");

            updateListView();


        }catch (InflateException e){

        }

        return view;
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
            HttpGet request = new HttpGet("http://api.seedmcot.com/api/song-details");
            request.setHeader("Content-Type", "text/xml");
            HttpResponse response;
            try {
                response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();
                String result = ((MainActivity) getActivity()).convertinputStreamToString(instream);
                Log.e("system", "Sucess!!!!");
                Log.e("system", "DATA List" + result);

                JSONArray jsonArray = new JSONArray(result);
                for(int x= 0 ; x < jsonArray.length() ; ++x){
                    JSONObject object = jsonArray.getJSONObject(x);
                    Gson gson = new Gson();
                    ListPageItem listPageItem = gson.fromJson(object.toString(), ListPageItem.class);
                    listPageItems.add(listPageItem);
                }
            } catch (Exception e) {
                Log.e("system", "Error!!!!");
                Log.e("system", e.getMessage());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
