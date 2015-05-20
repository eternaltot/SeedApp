package com.seedmcot.seedcave;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by TOT on 25/2/2558.
 */
public class CustomAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> group;
    private static LayoutInflater inflater=null;
    public Resources res;
    public Boolean livePage = false;


    public CustomAdapter(Activity a, ArrayList<String> d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        group=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public CustomAdapter(Activity a, ArrayList<String> d,Resources resLocal, Boolean livePage) {

        /********** Take passed values **********/
        activity = a;
        group=d;
        res = resLocal;
        this.livePage = livePage;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return group.size();
    }

    @Override
    public Object getItem(int position) {
        return group.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if(!livePage)
                convertView = inflater.inflate(R.layout.listitem, null);
            else
                convertView = inflater.inflate(R.layout.listitem_live_rerun, null);
        }
        if(position % 2 == 0){
            convertView.setBackgroundColor(Color.parseColor("#f46555"));
        }else{
            convertView.setBackgroundColor(Color.parseColor("#f38d76"));
        }

        if(!livePage) {
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(group.get(position));
        }else{
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText("Rerun : " + group.get(position));
        }
        return convertView;
    }
}
