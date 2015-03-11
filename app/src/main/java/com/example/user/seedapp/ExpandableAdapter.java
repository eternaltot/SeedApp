package com.example.user.seedapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.seedapp.com.add.model.Privilege;
import com.example.user.seedapp.com.add.model.Privilege_Child;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TOT on 25/2/2558.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {


    private Activity activity;
    private JSONArray group;
    private static LayoutInflater inflater=null;
    public Resources res;
    public Boolean livePage = false;
    private ArrayList<Privilege> listGroup = new ArrayList<>();
    private ArrayList<Privilege_Child> listChild;
    private HashMap<Privilege,ArrayList<Privilege_Child>> hashMap = new HashMap<Privilege,ArrayList<Privilege_Child>>();

    public ExpandableAdapter(Activity a, JSONArray d,Resources resLocal) throws JSONException, IOException {

        /********** Take passed values **********/
        activity = a;
        group=d;
        res = resLocal;

        for (int i = 0 ; i < group.length();i++){
            Privilege privilege = new Privilege();
            String s = MainActivity.path_Image_Privilege + (String)group.getJSONObject(i).get("small_image");
            URL newurl = new URL(s);
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            privilege.setBitmap(mIcon_val);
            Privilege_Child privilege_child = new Privilege_Child();
            String image_big = MainActivity.path_Image_Privilege_Child + (String)group.getJSONObject(i).get("big_image");
            String url = (String)group.getJSONObject(i).get("url") != null ? (String)group.getJSONObject(i).get("url") : "http://www.google.co.th";
            URL newurl_big = new URL(image_big);
            Bitmap big_image = BitmapFactory.decodeStream(newurl_big.openConnection().getInputStream());
            privilege_child.setBitmap(big_image);
            privilege_child.setUrl(url);
            listGroup.add(privilege);
            listChild = new ArrayList<Privilege_Child>();
            listChild.add(privilege_child);
            hashMap.put(privilege,listChild);
        }

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getGroupCount() {
        return listGroup.size();
    }

    public int getChildrenCount(int groupPosition) {
        return hashMap.get(listGroup.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return hashMap.get(listGroup.get(groupPosition));
    }

    public long getGroupId(int groupPosition) {
        return 0;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem, null);
        }
        if(groupPosition % 2 == 0){
//            convertView.setBackgroundColor(Color.parseColor("#f46555"));
        }else{
//            convertView.setBackgroundColor(Color.parseColor("#f38d76"));
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageItem);
        imageView.setImageBitmap(listGroup.get(groupPosition).getBitmap());
        Log.d("system","Render List Group :: " + listGroup.get(groupPosition).getBitmap().getByteCount());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listchild_item, null);
        }
        if(groupPosition % 2 == 0){
//            convertView.setBackgroundColor(Color.parseColor("#f46555"));
        }else{
//            convertView.setBackgroundColor(Color.parseColor("#f38d76"));
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageChildItem);
        imageView.setImageBitmap(((ArrayList<Privilege_Child>)hashMap.get(listGroup.get(groupPosition))).get(0).getBitmap());
//        String url = ((ArrayList<Privilege_Child>)hashMap.get(groupPosition)).get(0).getUrl();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        Log.d("system","Render List Child :: " + url);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
