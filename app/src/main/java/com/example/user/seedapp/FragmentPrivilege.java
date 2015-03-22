package com.example.user.seedapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by TOT on 22/2/2558.
 */
public class FragmentPrivilege extends Fragment {
    private static View view;
    private ArrayList<String> listitem = new ArrayList<String>();
    private ArrayList<String> listchild = new ArrayList<String>();
    private ExpandableListView expandableListView;
    private ExpandableAdapter adapter;
    FragmentTransaction transaction;

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
            final Resources res =getResources();
            adapter = new ExpandableAdapter(getActivity(),((MainActivity)getActivity()).getDataPrivillege(),res);
            expandableListView.setAdapter(adapter);
            expandableListView.setDivider(null);
            expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    SelectItemFragment fragment = new SelectItemFragment();
                    ((MainActivity)getActivity()).setFragment(fragment);
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
                            try {
                                adapter = new ExpandableAdapter(getActivity(),((MainActivity)getActivity()).getDataPrivillege(),res);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            expandableListView.setAdapter(adapter);
                        }
                    },3000);
                }
            });
        } catch (Exception e) {
            Log.e("system", "Error onCreateView FragmentPrivilege");
            Log.e("system", e.getMessage());
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
