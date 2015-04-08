package com.example.user.seedapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.seedapp.com.add.model.Music;
import com.example.user.seedapp.com.add.view.AutoScrollViewPager;


/**
 * Created by LacNoito on 2/10/2015.
 */
public class FragmentMain extends Fragment {

    private static View view;
    private ImageButton bt_youtube;
    private ImageButton bt_lyrics;
    private ImageButton bt_list;
    private ImageButton bt_mute;
    private ImageButton bt_play;
    private TextView textNowPlaying;
    private TextView textNameSong;
    private TextView nowPlaying;
    private TextView next;
    private MainActivity mainActivity;
    private static PlayMedia play;
    private static FragmentListPage fragmentListPage;
    private SeekBar seekbar;
    private AudioManager audioManager;
    private ImageView imageView3;

    public void updateNowPlayingAndNext(String now, String next,String pathImage,String url){
        final String url_Link = url;
        try {
            Log.d("system ", "textNowPlaying ::" + now);
            Log.d("system ", "textNextSong :: " + next);
            textNowPlaying.setText(now);
            textNameSong.setText(next);
            if(pathImage!=null && !pathImage.equals(""))
                Glide.with(mainActivity).load(MainActivity.path_Image_Cover_NowPlaying + pathImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView3);

            if(url != null && !url.equals("")){
                imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mainActivity,WebviewActivity.class);
                        intent.putExtra("URL", url_Link);
                        Bundle bundle = ActivityOptions.makeCustomAnimation(mainActivity, R.anim.slide_in_up, R.anim.slide_out_up).toBundle();
                        mainActivity.startActivity(intent, bundle);
                    }
                });
            }


            if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowLyric()!=null && !mainActivity.getCurrentPlay().getNowLyric().equals(""))){
                setDisableButtonLyric(true);
            }else if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowLyric()==null || mainActivity.getCurrentPlay().getNowLyric().equals(""))){
                setDisableButtonLyric(false);
            }
            if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowMv()!=null && !mainActivity.getCurrentPlay().getNowMv().equals(""))){
                setDisableButtonMV(true);
            }else if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowMv()==null || mainActivity.getCurrentPlay().getNowMv().equals(""))){
                setDisableButtonMV(false);
            }
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

    public void sendEventClickPlay(){
        if(bt_play!=null)
        bt_play.performClick();
    }

    public void pauseMedia(){
        if(play != null && play.returnIsPlating()){
            play.playMedia(false);
        }
    }

    public void setDisableButtonMV(Boolean isEnable){
        if(bt_youtube!=null) {
            bt_youtube.setEnabled(isEnable);
        }
        if (!isEnable){
            bt_youtube.setImageResource(R.drawable.lock_mv);
        }else{
            bt_youtube.setImageResource(R.drawable.unlock_mv);
        }
    }
    public void setDisableButtonList(Boolean isEnable){
        if (bt_list!=null){
            bt_list.setEnabled(isEnable);
            if (!isEnable){
//                bt_list.setBackgroundColor(Color.DKGRAY);
                bt_list.setImageResource(R.drawable.lock_list);
            }
        }
    }
    public void setDisableButtonLyric(Boolean isEnable){
        if (bt_lyrics!=null){
            bt_lyrics.setEnabled(isEnable);
            if (!isEnable){
                bt_lyrics.setImageResource(R.drawable.lock_lylics);
            }else{
                bt_lyrics.setImageResource(R.drawable.unlock_lylics);
            }

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
            nowPlaying = (TextView) view.findViewById(R.id.now_playing);
            next = (TextView) view.findViewById(R.id.textView3);

            mainActivity = (MainActivity)getActivity();
            mainActivity.setTypeFace(nowPlaying);
            mainActivity.setTypeFace(next);

            Log.d("system","DJ List : " + mainActivity.getDJInfos().size());
            DJPageAdapter adapter = new DJPageAdapter(getChildFragmentManager(), mainActivity.getDJInfos());
            AutoScrollViewPager pager = (AutoScrollViewPager) view.findViewById(R.id.pager);
            pager.setAdapter(adapter);
            if(mainActivity.getDJInfos().size() != 0 && mainActivity.getDJInfos().size() != 1) {
                pager.startAutoScroll();
                pager.setCycle(true);
                pager.setInterval(15000);
            }

            bt_youtube = (ImageButton) view.findViewById(R.id.bt_youtube);
            bt_lyrics = (ImageButton) view.findViewById(R.id.bt_lyrics);
            bt_play = (ImageButton) view.findViewById(R.id.bt_play);
            bt_list = (ImageButton) view.findViewById(R.id.bt_list);
            bt_mute = (ImageButton) view.findViewById(R.id.bt_mute);
            imageView3 = (ImageView) view.findViewById(R.id.imageView3);
            textNowPlaying = (TextView) view.findViewById(R.id.textNowPlaying);
            textNameSong = (TextView) view.findViewById(R.id.textNameSong);
            seekbar = (SeekBar) view.findViewById(R.id.seekBar);
            audioManager = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
            seekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            textNameSong.setSelected(true);

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

            if(mainActivity.getSeekBar() == null)
                mainActivity.setSeekBar(seekbar);
            else{
                seekbar.setProgress(mainActivity.getSeekBar().getProgress());
                mainActivity.setSeekBar(seekbar);
            }
            bt_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)!=0) {
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                        bt_mute.setImageResource(R.drawable.speaker_mute_white);
                    }
                    else {
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                        bt_mute.setImageResource(R.drawable.speaker);
                    }

                }
            });

            String now="";
            String next="";
            String pathImage="";
            String url_Link = "";
            if(mainActivity!=null){
                if(mainActivity.getCurrentPlay()!=null && mainActivity.getCurrentPlay().getEvent_type().equals("song")){
                    now = (mainActivity.getCurrentPlay().getSongTitle()!=null ? mainActivity.getCurrentPlay().getSongTitle():"");
                    pathImage = mainActivity.getCurrentPlay().getSongCover() != null ? mainActivity.getCurrentPlay().getSongCover() : "";
                }else if(mainActivity.getCurrentPlay().getEvent_type().equals("link")){
                    now = (mainActivity.getCurrentPlay().getLink_title()!=null ? mainActivity.getCurrentPlay().getLink_title():"");
                    pathImage = mainActivity.getCurrentPlay().getLinkCover() != null ? mainActivity.getCurrentPlay().getLinkCover() : "";
                    url_Link = mainActivity.getCurrentPlay().getLinkUrl();
                }
                if(mainActivity.getNextPlay()!=null && mainActivity.getNextPlay().getEvent_type().equals("song")){
                    next = (mainActivity.getNextPlay().getSongTitle()!=null ? mainActivity.getNextPlay().getSongTitle():"");
                }else if(mainActivity.getNextPlay().getEvent_type().equals("link")){
                    next = (mainActivity.getNextPlay().getLink_title()!=null ? mainActivity.getNextPlay().getLink_title():"");
                }
                if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowLyric()!=null && !mainActivity.getCurrentPlay().getNowLyric().equals(""))){
                    setDisableButtonLyric(true);
                }else if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowLyric()==null || mainActivity.getCurrentPlay().getNowLyric().equals(""))){
                    setDisableButtonLyric(false);
                }
                if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowMv()!=null && !mainActivity.getCurrentPlay().getNowMv().equals(""))){
                    setDisableButtonMV(true);
                }else if(mainActivity.getCurrentPlay()!=null && (mainActivity.getCurrentPlay().getNowMv()==null || mainActivity.getCurrentPlay().getNowMv().equals(""))){
                    setDisableButtonMV(false);
                }
            }

            updateNowPlayingAndNext(now,next,pathImage,url_Link);
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
//                bt_play.setBackgroundColor(Color.WHITE);
                bt_play.setImageResource(R.drawable.pause_button);
            }


        }catch (Exception e){

        }
        if (bt_play.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.play_button).getConstantState())){
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

                            if (bt_play.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.play_button).getConstantState())){
//                                bt_play.setBackgroundColor(Color.WHITE);
                                bt_play.setImageResource(R.drawable.pause_button);

//                                MediaPlayer mediaPlayer = MediaPlayer.create(mainActivity.returnBaseContext(), R.raw.body_slam);
//                                mediaPlayer.start();
                                play.playMedia(true);
                            }else {
//                                bt_play.setBackgroundColor(Color.WHITE);
                                bt_play.setImageResource(R.drawable.play_button);

                                play.playMedia(false);
                            }
                        }
                    });

                    if(play != null && !play.returnIsPlating()) {
                        play.playMedia(Boolean.TRUE);
//                        bt_play.setBackgroundColor(Color.WHITE);
                        bt_play.setImageResource(R.drawable.pause_button);
                    }
                }
            });
        }
    }
}
