package com.seedmcot.seedcave;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by User on 30/3/2558.
 */
public class StreamFragment extends Fragment {
    private View view;
    private VideoView videoView;
    private ProgressDialog progressDialog;
    private String url;
    private MediaController mediacontroller;

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
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Video Streaming");
            progressDialog.setMessage("Loading Streaming...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            // Show progressbar
            progressDialog.show();

            try {
                // Start the MediaController
                mediacontroller = new MediaController(
                        getActivity());
                mediacontroller.setAnchorView(videoView);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(url);
                videoView.setMediaController(mediacontroller);
                videoView.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    videoView.start();
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    progressDialog.dismiss();
                    videoView.stopPlayback();
                    videoView.setVideoURI(null);
                    return false;
                }
            });
        }catch (Exception e){
            progressDialog.dismiss();
        }
        return view;
    }

    public void loadVideo(){
        progressDialog.show();
        videoView.stopPlayback();
        Uri video = Uri.parse(url);
        videoView.setVideoURI(video);
    }
}
