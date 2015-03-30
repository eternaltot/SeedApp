package com.example.user.seedapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by User on 30/3/2558.
 */
public class StreamFragment extends Fragment {
    private View view;
    private VideoView videoView;
    private ProgressDialog progressDialog;
    private String url;
    private MediaPlayer mediaPlayer;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view!=null){
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup!=null){
                viewGroup.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.stream_fragment, container, false);
            videoView = (VideoView) view.findViewById(R.id.VideoView);
//            progressDialog = new ProgressDialog(getActivity());
//            // Set progressbar title
//            progressDialog.setTitle("Video Streaming");
//            // Set progressbar message
//            progressDialog.setMessage("Buffering...");
//            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
//            // Show progressbar
//            progressDialog.show();

            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(
                        getActivity());
                // Get the URL from String VideoURL

                videoView.setMediaController(mediacontroller);
                Log.d("debug" , "Stream URL " + url);
                videoView.setVideoPath(url);
                videoView.requestFocus();
                videoView.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(io.vov.vitamio.MediaPlayer mp) {
                        mp.setPlaybackSpeed(1.0f);
//                        progressDialog.dismiss();
                        videoView.start();
                    }
                });

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


        }catch (Exception e){
            Log.e("Error" , e.getMessage());
        }
        return view;
    }
}
