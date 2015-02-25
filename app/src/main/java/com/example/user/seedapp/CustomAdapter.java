package com.example.user.seedapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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
public class CustomAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<String> group;
    private ArrayList<String> child;
    private static LayoutInflater inflater=null;
    public Resources res;

    public CustomAdapter(Activity a, ArrayList<String> d,ArrayList<String> c,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        group=d;
        child = c;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listitem, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(group.get(groupPosition));
            return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView text = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listchild, null);
            }
            text = (TextView) convertView.findViewById(R.id.textchild);
            final String temp = child.get(childPosition);
            text.setText(temp);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,temp,
                            Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
