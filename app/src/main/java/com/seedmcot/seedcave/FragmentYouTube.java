package com.seedmcot.seedcave;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by LacNoito on 2/13/2015.
 */
public class FragmentYouTube extends Fragment {

    private static View view;
    private FragmentActivity myContext;
    private YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyCjfgiAytO0iYrnz7EQuWarGLSSPmW_mw0";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static String youtubeName = "l8Iu9PEKMmw";
    private static String musicName = "-";
    private MainActivity mainActivity;
    private ImageView back_bt;
    private TextView tv_name;
    private TextView nowPlaying;

    public static void setMusicName(String musicName) {
        FragmentYouTube.musicName = musicName;
    }

    public void setYoutubeName(String youtubeName){
        this.youtubeName = youtubeName;
    }

    public void setTv_name(String text){
        if(tv_name!=null && text!=null){
            tv_name.setText(text);
        }

        musicName = text;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        if (activity instanceof FragmentActivity) {
//            myContext = (FragmentActivity) activity;
//        }
//
//        super.onAttach(activity);
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_youtube, container, false);

            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setSelected(true);
            nowPlaying = (TextView) view.findViewById(R.id.now_playing);
            mainActivity = (MainActivity) getActivity();

//            getDataFromServer();

            mainActivity.pauseMediaFromMainActivity();
            mainActivity.setTypeFace(nowPlaying);
            tv_name.setText(musicName);


            back_bt = (ImageView) view.findViewById(R.id.back_bt);

            back_bt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mainActivity.setFragmentNoBack(mainActivity.getFragmentMain());
                }
            });

            YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

            youTubePlayerFragment.initialize(YoutubeDeveloperKey, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                    if (!b) {
                        YPlayer = youTubePlayer;
                        YPlayer.setFullscreen(false);
                        YPlayer.loadVideo(youtubeName);
                        YPlayer.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                    // TODO Auto-generated method stub

                }
            });
        }catch (InflateException e){

        }

        setBackEvent();

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
