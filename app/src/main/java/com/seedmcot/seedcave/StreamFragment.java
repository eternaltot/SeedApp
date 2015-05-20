package com.seedmcot.seedcave;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.VideoSurfaceView;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.google.android.exoplayer.util.PlayerControl;
import com.google.android.exoplayer.util.Util;
import com.seedmcot.seedcave.add.player.DemoPlayer;
import com.seedmcot.seedcave.add.player.HlsRendererBuilder;
import com.seedmcot.seedcave.add.player.SmoothStreamingRendererBuilder;


/**
 * Created by User on 30/3/2558.
 */
public class StreamFragment extends Fragment implements SurfaceHolder.Callback,AudioCapabilitiesReceiver.Listener {
    private View view;
    private VideoSurfaceView videoView;
    private ProgressDialog progressDialog;
    private String url;
    private DemoPlayer player;
    private MediaController mediacontroller;
    private AudioCapabilities audioCapabilities;
    private AudioCapabilitiesReceiver audioCapabilitiesReceiver;
    private String userAgent;

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
            videoView = (VideoSurfaceView) view.findViewById(R.id.VideoView);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Video Streaming");
            progressDialog.setMessage("Loading Streaming...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            // Show progressbar
//            progressDialog.show();

            try {
                // Start the MediaController
                userAgent = Util.getUserAgent(getActivity(), "SeedMCOT");
                player = new DemoPlayer(new HlsRendererBuilder(getActivity(), userAgent, url.toString(), null,
                        audioCapabilities));
                player.setBackgrounded(false);
                mediacontroller = new MediaController(
                        getActivity());
                mediacontroller.setAnchorView(videoView);
                mediacontroller.setMediaPlayer(player.getPlayerControl());
                mediacontroller.setEnabled(true);
//                // Get the URL from String VideoURL
//                Uri video = Uri.parse(url);
//                videoView.setMediaController(mediacontroller);
//                videoView.setVideoURI(video);
                videoView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            toggleControlsVisibility();
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.performClick();
                        }
                        return true;
                    }
                });
                videoView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                            return mediacontroller.dispatchKeyEvent(event);
                        }
                        return false;
                    }
                });
                audioCapabilitiesReceiver = new AudioCapabilitiesReceiver(getActivity(), this);
                videoView.getHolder().addCallback(this);
                player.prepare();
//                progressDialog.dismiss();
                player.setSurface(videoView.getHolder().getSurface());
                player.setPlayWhenReady(true);


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

//            videoView.requestFocus();
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                // Close the progress bar and play the video
//                public void onPrepared(MediaPlayer mp) {
//                    progressDialog.dismiss();
//                    videoView.start();
//                }
//            });
//            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                @Override
//                public boolean onError(MediaPlayer mp, int what, int extra) {
//                    progressDialog.dismiss();
//                    videoView.stopPlayback();
//                    videoView.setVideoURI(null);
//                    return false;
//                }
//            });
        }catch (Exception e){
//            progressDialog.dismiss();
        }
        return view;
    }

    public void loadVideo(){
//        progressDialog.show();
        releasePlayer();
        preparePlayer();
//        progressDialog.dismiss();
    }

    private void toggleControlsVisibility()  {
        if (mediacontroller.isShowing()) {
            mediacontroller.hide();
        } else {
            showControls();
        }
    }

    private void showControls() {
        mediacontroller.show(0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player != null) {
            player.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null) {
            player.blockingClearSurface();
        }
    }

    @Override
    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        boolean audioCapabilitiesChanged = !audioCapabilities.equals(this.audioCapabilities);
        if (player == null || audioCapabilitiesChanged) {
            this.audioCapabilities = audioCapabilities;
            releasePlayer();
            preparePlayer();
        } else if (player != null) {
            player.setBackgrounded(false);
        }
    }

    private void preparePlayer() {
        if (player == null) {
            player = new DemoPlayer(new HlsRendererBuilder(getActivity(), userAgent, url.toString(), null,
                    audioCapabilities));
            player.setBackgrounded(false);
            mediacontroller.setMediaPlayer(player.getPlayerControl());
            mediacontroller.setEnabled(true);

        }
        player.prepare();

        player.setSurface(videoView.getHolder().getSurface());
        player.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroyView() {
        releasePlayer();
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();
    }

    @Override
    public void onResume() {
        releasePlayer();
        preparePlayer();
        super.onResume();
    }
}
