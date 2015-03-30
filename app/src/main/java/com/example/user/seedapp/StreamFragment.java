package com.example.user.seedapp;

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
            // Set progressbar title
            progressDialog.setTitle("Video Streaming");
            // Set progressbar message
            progressDialog.setMessage("Buffering...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            // Show progressbar
            progressDialog.show();

            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(
                        getActivity());
                mediacontroller.setAnchorView(videoView);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(url);
                videoView.setMediaController(mediacontroller);
                videoView.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    videoView.start();
                }
            });
        }catch (Exception e){

        }
        return view;
    }
}
