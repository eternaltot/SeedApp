package com.example.user.seedapp;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.seedapp.com.add.model.ListPageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LacNoito on 3/8/2015.
 */
public class ListPageAdapter extends BaseAdapter {

    private Activity activity;
    private List<ListPageItem> listPageItems = new ArrayList<ListPageItem>() ;
    private static LayoutInflater inflater=null;

    public ListPageAdapter(Activity a, List<ListPageItem> listPageItems){
        this.listPageItems = listPageItems;
        activity = a;

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
        textView.setText(listPageItems.get(position).getTitle());

        return convertView;
    }
}
