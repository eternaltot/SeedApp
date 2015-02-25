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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by TOT on 22/2/2558.
 */
public class FragmentPrivilege extends Fragment {
    private static View view;
    private ArrayList<String> listitem = new ArrayList<String>();
    private ArrayList<String> listchild = new ArrayList<String>();
    private ListView expandableListView;
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
            expandableListView = (ListView) view.findViewById(R.id.listView);
            final Resources res =getResources();
            adapter = new CustomAdapter(getActivity(),listitem,res);
            expandableListView.setAdapter(adapter);
            expandableListView.setDivider(null);
            expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            adapter = new CustomAdapter(getActivity(),listitem,res);
                            expandableListView.setAdapter(adapter);
                        }
                    },5000);
                }
            });

        }catch (InflateException e){

        }

        return view;
    }

    public void setListData(){
        listitem = new ArrayList<String>();
        listchild = new ArrayList<String>();
        listitem.add("PRIVILEGE");
        listitem.add("PRIVILEGE");
        listitem.add("PRIVILEGE");
        listitem.add("PRIVILEGE");
        listitem.add("PRIVILEGE");
        listitem.add("PRIVILEGE");
    }
}
