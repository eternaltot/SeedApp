package com.example.user.seedapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by LacNoito on 3/4/2015.
 */
public class PlayMedia {
    public static MediaPlayer mediaPlayer;
    private String url = "";
    private Boolean flagStop = Boolean.FALSE;
    private Runnable runnable;
    private Handler mLeakyHandler;
    private ImageButton bt_play;
    private GifImageView bt_play_load;

    public ImageButton getBt_play() {
        return bt_play;
    }

    public void setBt_play(ImageButton bt_play) {
        this.bt_play = bt_play;
    }

    public void setBt_play_load(GifImageView bt_play_load) {
        this.bt_play_load = bt_play_load;
    }

    public PlayMedia(String url, Context context, Handler mHandler, ImageButton bt_play, GifImageView bt_play_load) {

        mediaPlayer = new MediaPlayer();
        this.mLeakyHandler = mHandler;
        this.bt_play = bt_play;
        this.bt_play_load = bt_play_load;

        try {

            this.url = url;
            setReset();

            runnable = new Runnable() {
                @Override
                public void run() {
                    Log.d("system", "----Runnable---");
                    if(mediaPlayer != null && !mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        flagStop = Boolean.TRUE;
                        Log.d("system", "mediaPlayer.stop()");
                    }
                }
            };
        } catch (Exception e) {
            Log.e("system", "Error : " + e.getMessage());
        }
    }

    private void setReset() throws Exception{
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
    }

    public Boolean returnIsPlating(){
        return mediaPlayer.isPlaying();
    }

    public void playStart(){
        try {
            if (flagStop)
                setReset();
            mediaPlayer.start();
            flagStop = Boolean.FALSE;
            bt_play.setVisibility(View.VISIBLE);
            bt_play_load.setVisibility(View.GONE);
        }catch (Exception ex){

        }
    }

    public Boolean getFlagStop() {
        return flagStop;
    }

    public void playMedia(boolean check) {
        try {
            if (check) {
                if(flagStop) {
                    setReset();
                }
                mediaPlayer.start();
                flagStop = Boolean.FALSE;
                mLeakyHandler.removeCallbacks(runnable);
            } else {
                mediaPlayer.pause();

                mLeakyHandler.postDelayed(runnable, 5000);
            }
        } catch (Exception e) {
            Log.e("system", "Error ::: " + e.getMessage());
        }

        bt_play.setVisibility(View.VISIBLE);
        bt_play_load.setVisibility(View.GONE);
    }
}
