package com.seedmcot.seedcave;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seedmcot.seedcave.add.model.ListPageItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LacNoito on 3/8/2015.
 */
public class ListPageAdapter extends BaseAdapter {

    private Activity activity;
    private List<ListPageItem> listPageItems = new ArrayList<ListPageItem>() ;
    private static LayoutInflater inflater=null;
    private MainActivity mainActivity;

    public ListPageAdapter(Activity a, List<ListPageItem> listPageItems){
        this.listPageItems = listPageItems;
        activity = a;
        mainActivity = (MainActivity) activity;
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listPageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listPageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_page_item, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        if(mainActivity!=null){
            mainActivity.setTypeFaceOther(textView);
        }
        ListPageItem item = listPageItems.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate=null;
        try {
            startDate = sdf.parse(item.getActual_date_time());
//            String newDateString = sdf.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = "HH:mm";
        sdf = new SimpleDateFormat(format);
        String time = startDate != null ? sdf.format(startDate): "";
        String title = item.getEvent_type()!=null && item.getEvent_type().equals("song") ? item.getSongTitle() + " - " + item.getArtistName() : item.getLink_title() != null ? item.getLink_title() : "Seed 97.5 FM";
        textView.setText(time+" "+title);

        return convertView;
    }
}
