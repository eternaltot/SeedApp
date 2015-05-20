package com.seedmcot.seedcave;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
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
    private ExpandableAdapter adapter;
    private int lastExpandedPosition = -1;
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
            expandableListView = (ExpandableListView) view.findViewById(R.id.listView);
            setBackEvent();
            final Resources res =getResources();
            adapter = new ExpandableAdapter(getActivity(),((MainActivity)getActivity()).getDataPrivillege(),res,expandableListView);
            expandableListView.setAdapter(adapter);
            expandableListView.setDivider(null);

            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (lastExpandedPosition != -1
                            && groupPosition != lastExpandedPosition) {
                        expandableListView.collapseGroup(lastExpandedPosition);
                    }
                    lastExpandedPosition = groupPosition;
//                    expandableListView.smoothScrollToPositionFromTop(groupPosition,10,2000);
//                    expandableListView.setSelectionFromTop(groupPosition,10);
                    expandableListView.setSelection(groupPosition);
//                    final int pos = groupPosition;
//                    expandableListView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            expandableListView.smoothScrollToPosition(pos);
//                        }
//                    });
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
                                adapter = new ExpandableAdapter(getActivity(),((MainActivity)getActivity()).getDataPrivillege(),res,expandableListView);
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

    public void setBackEvent(){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i("system", "keyCode: " + keyCode);
//                if( keyCode == KeyEvent.KEYCODE_BACK ) {
//                    mainActivity.setFragmentNoBack(mainActivity.getFragmentMain());
//                    return true;
//                } else {
//                    return false;
//                }
                return view.onKeyDown(keyCode, event);
            }
        });
    }

}
