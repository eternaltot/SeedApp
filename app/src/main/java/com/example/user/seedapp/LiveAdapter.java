package com.example.user.seedapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.seedapp.com.add.model.ItemLive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LacNoito on 3/17/2015.
 */
public class LiveAdapter extends BaseAdapter {

    private Activity activity;
    private MainActivity mainActivity;
    public Resources res;
    private static LayoutInflater inflater=null;
    private List<ItemLive> itemLiveList = new ArrayList<ItemLive>();

    public LiveAdapter(Activity a,Resources resLocal, List<ItemLive> itemLiveList) {
        activity = a;
        mainActivity = (MainActivity) a;
        res = resLocal;
        this.itemLiveList = itemLiveList;

        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return itemLiveList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemLiveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem_live_rerun, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(itemLiveList.get(position).getTitle());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
        if(itemLiveList.get(position).getType().equals("0"))
        Glide.with(convertView.getContext().getApplicationContext()).load(MainActivity.path_Thumbnail_Youtube + itemLiveList.get(position).getThumbnail()+"/default.jpg").diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        else imageView.setImageResource(R.drawable.seed_empty);


        return convertView;
    }
}
