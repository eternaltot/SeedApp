package com.seedmcot.seedcave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.seedmcot.seedcave.add.model.Privilege;
import com.seedmcot.seedcave.add.model.Privilege_Child;

import org.json.JSONArray;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TOT on 25/2/2558.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {


    private Activity activity;
    private MainActivity mainActivity;
    private JSONArray group;
    private static LayoutInflater inflater=null;
    public Resources res;
    public Boolean livePage = false;
    private ArrayList<Privilege> listGroup = new ArrayList<>();
    private ArrayList<Privilege_Child> listChild;
    private HashMap<Privilege,ArrayList<Privilege_Child>> hashMap = new HashMap<Privilege,ArrayList<Privilege_Child>>();
    private int height;
    private int width;
    private ExpandableListView listView;

    public ExpandableAdapter(Activity a, JSONArray d,Resources resLocal,ExpandableListView listView) {

        try {
            /********** Take passed values **********/
            activity = a;
            group = d;
            res = resLocal;
            mainActivity = (MainActivity) activity;
            this.listView = listView;

            DisplayMetrics displaymetrics = new DisplayMetrics();
            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            height = displaymetrics.heightPixels;
            width = displaymetrics.widthPixels;



            for (int i = 0; i < group.length(); i++) {
                Privilege privilege = new Privilege();
                String s = MainActivity.path_Image_Privilege + (String) group.getJSONObject(i).get("small_image");
                privilege.setBitmap(mainActivity.drawableManagerTT.drawableBitMap(s));
                Privilege_Child privilege_child = new Privilege_Child();
                String image_big = MainActivity.path_Image_Privilege_Child + (String) group.getJSONObject(i).get("big_image");
                String url = (String) group.getJSONObject(i).get("url") != null ? (String) group.getJSONObject(i).get("url") : "http://www.google.co.th";
                privilege_child.setUrl(url);
                privilege_child.setUrlImage(image_big);
                mainActivity.drawableManagerTT.fetchOnThread(image_big);
                listGroup.add(privilege);
                listChild = new ArrayList<Privilege_Child>();
                listChild.add(privilege_child);
                hashMap.put(privilege, listChild);
            }

            /***********  Layout inflator to call external xml layout () ***********/
            inflater = (LayoutInflater) activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }catch (Exception ex){
            Log.e("system", "Error onCreateView ExpandableAdapter");
            Log.e("system", ex.getMessage());
        }
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



    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem, null);
        }
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageItem);
        imageView.setImageBitmap(listGroup.get(groupPosition).getBitmap());
        if (isExpanded)
            convertView.setPadding(0, 0, 0, 0);
        else
            convertView.setPadding(0, 0, 0, 10);

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float f = ((float)h)/w;
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (new BigDecimal((width * f))).intValue();
        imageView.setLayoutParams(layoutParams);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listchild_item, null);
        }
        final int gposition=groupPosition;
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageChildItem);
//        Glide.with(mainActivity.getApplicationContext()).load(((ArrayList<Privilege_Child>) hashMap.get(listGroup.get(groupPosition))).get(0).getUrlImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        ImageLoader imageLoader = new ImageLoader(mainActivity);
        String url = hashMap.get(listGroup.get(groupPosition)).get(0).getUrlImage();
        imageLoader.DisplayImage(url,imageView);
//        imageView.setImageBitmap(getBitmapFromURL(((ArrayList<Privilege_Child>) hashMap.get(listGroup.get(groupPosition))).get(0).getUrlImage()));
//        mainActivity.drawableManagerTT.fetchDrawableOnThread(((ArrayList<Privilege_Child>) hashMap.get(listGroup.get(groupPosition))).get(0).getUrlImage(), imageView);
//        imageLoader.DisplayImage(((ArrayList<Privilege_Child>) hashMap.get(listGroup.get(groupPosition))).get(0).getUrlImage(), imageView);
//        imageView.setImageBitmap(((ArrayList<Privilege_Child>) hashMap.get(listGroup.get(groupPosition))).get(0).getBitmap());
//        String url = ((ArrayList<Privilege_Child>)hashMap.get(groupPosition)).get(0).getUrl();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity,WebviewActivity.class);
                intent.putExtra("URL", ((ArrayList<Privilege_Child>) hashMap.get(listGroup.get(gposition))).get(0).getUrl());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float f = ((float)h)/w;
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (new BigDecimal((width * f))).intValue();
        imageView.setLayoutParams(layoutParams);
        if (isLastChild) {
            convertView.setPadding(0, 10, 0, 10);
        } else
            convertView.setPadding(0, 10, 0, 0);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

        super.onGroupExpanded(groupPosition);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            // Log exception
            return null;
        }
    }
}
