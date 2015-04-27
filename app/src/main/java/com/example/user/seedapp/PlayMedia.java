package com.example.user.seedapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by LacNoito on 3/4/2015.
 */
public class PlayMedia {
    public MediaPlayer mediaPlayer;
    private String url = "";
    private Boolean flagStop = Boolean.FALSE;
    private Runnable runnable;
    private Handler mLeakyHandler;
    private ImageButton bt_play;

    public void setBt_play(ImageButton bt_play) {
        this.bt_play = bt_play;
    }

    public PlayMedia(String url, Context context, Handler mHandler, ImageButton bt_play) {

        mediaPlayer = new MediaPlayer();
        this.mLeakyHandler = mHandler;
        this.bt_play = bt_play;

        try {

            this.url = url;
            setReset();

            runnable = new Runnable() {
                @Override
                public void run() {
                    Log.d("system", "----Runnable---");
                    if(mediaPlayer != null && !mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
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
        }catch (Exception ex){

        }
    }

    public void playMedia(boolean check) {
        try {
            if (check) {
                if(flagStop)
                    setReset();
                mediaPlayer.start();
                flagStop = Boolean.FALSE;
                mLeakyHandler.removeCallbacks(runnable);
                bt_play.setEnabled(true);
            } else {
                mediaPlayer.pause();

                mLeakyHandler.postDelayed(runnable, 5000);
            }
        } catch (Exception e) {
            Log.e("system", "Error ::: " + e.getMessage());
        }
    }
}
