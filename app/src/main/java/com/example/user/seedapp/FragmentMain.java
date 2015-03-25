package com.example.user.seedapp;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.seedapp.com.add.model.Music;
import com.example.user.seedapp.com.add.view.AutoScrollViewPager;
import com.viewpagerindicator.CirclePageIndicator;


/**
 * Created by LacNoito on 2/10/2015.
 */
public class FragmentMain extends Fragment {

    private static View view;
    private Button bt_youtube;
    private Button bt_lyrics;
    private Button bt_list;
    private ImageButton bt_play;
    private TextView textNowPlaying;
    private TextView textNameSong;
    private MainActivity mainActivity;
    private static PlayMedia play;
    private static FragmentListPage fragmentListPage;
    private SeekBar seekbar;
    private AudioManager audioManager;

    public void updateNowPlayingAndNext(String now, String next){
        try {
            Log.d("system ", "textNowPlaying ::" + now);
            Log.d("system ", "textNextSong :: " + next);
            textNowPlaying.setText(now);
            textNameSong.setText(next);
        }catch (Exception ex) {

        }
    }

    public void updateListViewFragmentListPageFromFragmentMain(){
        if(fragmentListPage != null){
            fragmentListPage.updateListView();
        }
    }

    public void playMedia(){
        if(play != null){
            play.playMedia(false);
        }
    }

    public void pauseMedia(){
        if(play != null && play.returnIsPlating()){
            play.playMedia(false);
        }
    }

    public void setDisableButtonMV(Boolean isEnable){
        if(bt_youtube!=null)
            bt_youtube.setEnabled(isEnable);
    }
    public void setDisableButtonList(Boolean isEnable){
        if (bt_list!=null){
            bt_list.setEnabled(isEnable);
        }
    }
    public void setDisableButtonLyric(Boolean isEnable){
        if (bt_lyrics!=null){
            bt_lyrics.setEnabled(isEnable);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_main, container, false);

            mainActivity = (MainActivity)getActivity();

            Log.d("system","DJ List : " + mainActivity.getDJInfos().size());
            DJPageAdapter adapter = new DJPageAdapter(getChildFragmentManager(), mainActivity.getDJInfos());
            AutoScrollViewPager pager = (AutoScrollViewPager) view.findViewById(R.id.pager);
            pager.setAdapter(adapter);
            if(mainActivity.getDJInfos().size() != 0 && mainActivity.getDJInfos().size() != 1) {
                pager.startAutoScroll();
                pager.setCycle(true);
                pager.setInterval(15000);
            }

            bt_youtube = (Button) view.findViewById(R.id.bt_youtube);
            bt_lyrics = (Button) view.findViewById(R.id.bt_lyrics);
            bt_play = (ImageButton) view.findViewById(R.id.bt_play);
            bt_list = (Button) view.findViewById(R.id.bt_list);
            textNowPlaying = (TextView) view.findViewById(R.id.textNowPlaying);
            textNameSong = (TextView) view.findViewById(R.id.textNameSong);
            seekbar = (SeekBar) view.findViewById(R.id.seekBar);
            audioManager = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
            seekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                }
            };
            audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            String now="";
            String next="";
            if(mainActivity!=null){
                if(mainActivity.getCurrentPlay()!=null && mainActivity.getCurrentPlay().getEvent_type().equals("song")){
                    now = "Now Playing : " + (mainActivity.getCurrentPlay().getSongTitle()!=null ? mainActivity.getCurrentPlay().getSongTitle():"");
                }else if(mainActivity.getCurrentPlay().getEvent_type().equals("link")){
                    now = "Now Playing : " + (mainActivity.getCurrentPlay().getLink_title()!=null ? mainActivity.getCurrentPlay().getLink_title():"");
                }
                if(mainActivity.getNextPlay()!=null && mainActivity.getNextPlay().getEvent_type().equals("song")){
                    next = "Next : " + (mainActivity.getNextPlay().getSongTitle()!=null ? mainActivity.getNextPlay().getSongTitle():"");
                }else if(mainActivity.getNextPlay().getEvent_type().equals("link")){
                    next = "Next : "  + (mainActivity.getNextPlay().getLink_title()!=null ? mainActivity.getNextPlay().getLink_title():"");
                }
            }

            updateNowPlayingAndNext(now,next);
//            updateNowPlayingAndNext(mainActivity.getCurrentPlay() != null && mainActivity.getCurrentPlay().getSongTitle() != null ? mainActivity.getCurrentPlay().getSongTitle() : "", mainActivity.getNextPlay() != null && mainActivity.getNextPlay().getSongTitle() != null ? mainActivity.getNextPlay().getSongTitle() : "");



            bt_youtube.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mainActivity.getCurrentPlay().getNowMv() != null) {
                        String url = mainActivity.getCurrentPlay().getNowMv();
                        String[] strings = url.split(mainActivity.getCutURLYoutube());
                        Log.d("system" , "url MV :: " + url);
                        FragmentYouTube fragmentYouTube = new FragmentYouTube();
                        if (strings.length > 0) {
                            fragmentYouTube.setYoutubeName(strings[strings.length - 1]);
                        }
                        mainActivity.setFragmentNoBack(fragmentYouTube);
                    }else{
//                        Toast.makeText(getActivity().getApplicationContext(), "No", Toast.LENGTH_LONG).show();
                    }
                }
            });

            bt_lyrics.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FragmentLyrics fragmentLyrics = new FragmentLyrics();
                    if(mainActivity!=null){
                        if(mainActivity.getCurrentPlay()!=null && mainActivity.getCurrentPlay().getEvent_type().equals("song")){
                            Music music = new Music();
                            music.setLyrics(mainActivity.getCurrentPlay().getNowLyric());
                            music.setName(mainActivity.getCurrentPlay().getSongTitle() + " - " + mainActivity.getCurrentPlay().getNowAuthor());
                            fragmentLyrics.setMusic(music);
                        }
                    }
                    mainActivity.setFragmentNoBack(fragmentLyrics);
                }
            });

            bt_list.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    fragmentListPage = new FragmentListPage();
                    mainActivity.setFragmentNoBack(fragmentListPage);
                }
            });

            new PlayMediaTask().execute();

            if(play != null && play.returnIsPlating()){
                bt_play.setBackgroundColor(Color.WHITE);
                bt_play.setImageResource(R.drawable.pause_button);

            }


        }catch (Exception e){

        }
        if (bt_play.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.btn_play).getConstantState())){
            Log.d("system","before click button when create fragment");
            bt_play.callOnClick();
        }
        return view;
    }



    class PlayMediaTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            if(play == null)
                play = new PlayMedia(mainActivity.getURL(), mainActivity.returnBaseContext());

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {

                    bt_play.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.d("system","in onclick btn play");

                            if (bt_play.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.btn_play).getConstantState())){
                                bt_play.setBackgroundColor(Color.WHITE);
                                bt_play.setImageResource(R.drawable.pause_button);

//                                MediaPlayer mediaPlayer = MediaPlayer.create(mainActivity.returnBaseContext(), R.raw.body_slam);
//                                mediaPlayer.start();
                                play.playMedia(true);
                            }else {
                                bt_play.setBackgroundColor(Color.WHITE);
                                bt_play.setImageResource(R.drawable.btn_play);

                                play.playMedia(false);
                            }
                        }
                    });
                }
            });
        }
    }
}
