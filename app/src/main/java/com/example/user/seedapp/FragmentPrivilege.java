package com.example.user.seedapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;


/**
 * Created by TOT on 22/2/2558.
 */
public class FragmentPrivilege extends Fragment {
    private static View view;
    private ArrayList<String> listitem = new ArrayList<String>();
    private ArrayList<String> listchild = new ArrayList<String>();
    private ExpandableListView expandableListView;
    private CustomAdapter adapter;

    public FragmentPrivilege() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_list_privilege, container, false);
            setListData();
            expandableListView = (ExpandableListView) view.findViewById(R.id.listView);
            Resources res =getResources();
            adapter = new CustomAdapter(getActivity(),listitem,listchild,res);
            expandableListView.setAdapter(adapter);
            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },5000);
                }
            });

        }catch (InflateException e){

        }

        return view;
    }

    public void setListData(){
        listitem.add("Test");
        listitem.add("Test");
        listitem.add("Test");
        listchild.add("ttttttttttttttttttttttttttttttttttttttttttttt");
    }
}
