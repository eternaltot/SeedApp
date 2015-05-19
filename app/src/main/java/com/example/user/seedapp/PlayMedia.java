package com.example.user.seedapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.user.seedapp.com.add.player.DemoPlayer;
import com.example.user.seedapp.com.add.player.ExtractorRendererBuilder;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.util.Util;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by LacNoito on 3/4/2015.
 */
public class PlayMedia {
//    public static MediaPlayer mediaPlayer;
    private String url = "";
    private Boolean flagStop = Boolean.FALSE;
    private Runnable runnable;
    private Handler mLeakyHandler;
    private ImageButton bt_play;
    private GifImageView bt_play_load;
    public MainActivity mainActivity;
    public DemoPlayer player;

    public ImageButton getBt_play() {
        return bt_play;
    }

    public void setBt_play(ImageButton bt_play) {
        this.bt_play = bt_play;
    }

    public void setBt_play_load(GifImageView bt_play_load) {
        this.bt_play_load = bt_play_load;
    }

    public Boolean returnIsPlating(){
        if(player == null)
            return false;
        return player.getPlayWhenReady();
    }

    public PlayMedia(String url, Context context, Handler mHandler, ImageButton bt_play, GifImageView bt_play_load, MainActivity mainActivity) {

//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        this.mLeakyHandler = mHandler;
        this.mainActivity = mainActivity;
        this.bt_play = bt_play;
        this.bt_play_load = bt_play_load;

        try {

            this.url = url;
//            setReset();

//            preparePlayer();

            runnable = new Runnable() {
                @Override
                public void run() {
                    Log.d("system", "----Runnable---");
                    if(player != null && !player.getPlayWhenReady()){
                        releasePlayer();
                        flagStop = Boolean.TRUE;
                        Log.d("system", "mediaPlayer.stop()");
                    }
                }
            };
        } catch (Exception e) {
            Log.e("system", "Error : " + e.getMessage());
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void preparePlayer() {
        if(player == null){
            String userAgent = Util.getUserAgent(mainActivity.getBaseContext(), "ExoPlayerDemo");
            Uri uri = Uri.parse("http://203.147.16.93:8000/seedcave.mp3");
            player = new DemoPlayer(new ExtractorRendererBuilder(userAgent, uri, null, new Mp3Extractor()));
            player.prepare();
            player.setPlayWhenReady(true);
        }
    }

    public void playStart(){
        try {
//            if (flagStop)
//                setReset();
//            mediaPlayer.start();

            preparePlayer();
            flagStop = Boolean.FALSE;
            bt_play.setVisibility(View.VISIBLE);
            bt_play_load.setVisibility(View.GONE);
        }catch (Exception ex){

        }
    }

    public Boolean getFlagStop() {
        return flagStop;
    }

    public void force_pause(){
//        releasePlayer();
        player.setPlayWhenReady(false);
        mLeakyHandler.postDelayed(runnable, 5000);
    }

    public void playMedia(boolean check) {
        try {
            if (check) {
                if(flagStop) {
                    preparePlayer();
                }else{
                    player.setPlayWhenReady(true);
                }
//                mediaPlayer.seekTo(0);
//                mediaPlayer.start();
//                mainActivity.setFlagPause(false);
//                preparePlayer();
                flagStop = Boolean.FALSE;
                mLeakyHandler.removeCallbacks(runnable);
            } else {
//                mediaPlayer.pause();
//                mainActivity.setFlagPause(true);
//                releasePlayer();
                player.setPlayWhenReady(false);
                mLeakyHandler.postDelayed(runnable, 5000);
            }
        } catch (Exception e) {
            Log.e("system", "Error ::: " + e.getMessage());
        }

        bt_play.setVisibility(View.VISIBLE);
        bt_play_load.setVisibility(View.GONE);
    }
}
