package com.example.user.seedapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.webkit.URLUtil;

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

    public PlayMedia(String url, Context context) {

        mediaPlayer = new MediaPlayer();

        try {
//            mediaPlayer.reset();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setDataSource(url);
//            mediaPlayer.prepare();

            this.url = url;
//            setReset();
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

    private void setDataSource(String path) throws IOException {
        if (!URLUtil.isNetworkUrl(path)) {
            mediaPlayer.setDataSource(path);
        } else {
            URL url = new URL(path);
            URLConnection cn = url.openConnection();
            cn.connect();
            InputStream stream = cn.getInputStream();
            if (stream == null)
                throw new RuntimeException("stream is null");
            File temp = File.createTempFile("mediaplayertmp", "dat");
            String tempPath = temp.getAbsolutePath();
            FileOutputStream out = new FileOutputStream(temp);
            byte buf[] = new byte[128];
            do {
                int numread = stream.read(buf);
                if (numread <= 0)
                    break;
                out.write(buf, 0, numread);
            } while (true);
            mediaPlayer.setDataSource(tempPath);
            try {
                stream.close();
            }
            catch (IOException ex) {
                Log.e("system", "Error :: " + ex.getMessage());
            }
        }
    }

    public Boolean returnIsPlating(){
        return mediaPlayer.isPlaying();
    }


    public void playMedia(boolean check) {
        try {
            if (check) {
                setReset();
                mediaPlayer.start();
            } else {
                mediaPlayer.stop();
//                setReset();
            }
        } catch (Exception e) {
            Log.e("system", "Error ::: " + e.getMessage());
        }
    }
}
