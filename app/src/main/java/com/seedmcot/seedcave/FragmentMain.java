package com.seedmcot.seedcave;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.seedmcot.seedcave.add.model.Music;
import com.seedmcot.seedcave.add.view.AutoScrollViewPager;

import pl.droidsonroids.gif.GifImageView;


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
    private GifImageView bt_play_load;
    private TextView textNowPlaying;
    private TextView textNameSong;
    private TextView nowPlaying;
    private TextView next;
    private MainActivity mainActivity;
    private static PlayMedia play;
    private static FragmentListPage fragmentListPage;
    private int progressFromAudioManager;
    private SeekBar seekbar;
    private AudioManager audioManager;
    private ImageView imageView3;
    private int oldProgress;
    ImageLoader imageLoader = new ImageLoader(mainActivity);
    private Handler mHandler = new Handler();
    private Boolean flagClick = Boolean.FALSE;

    public Boolean getFlagClick() {
        return flagClick;
    }

    public void setFlagClick(Boolean flagClick) {
        this.flagClick = flagClick;
    }

    public int getOldProgress() {
        return oldProgress;
    }

    public void setOldProgress(int oldProgress) {
        this.oldProgress = oldProgress;
    }

    @Override
    public void onResume() {
//        seekbar.setProgress(mainActivity.getSeekVal());
//        seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        if(flagClick)
            seekbar.setProgress(progressFromAudioManager);
        else{
            seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
        Log.d("system","Volume in fragmentmain 2 : " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        Log.d("system","State in Fragmentmain 2");
        super.onResume();
    }


    public void updateNowPlayingAndNext(String now, String next, String pathImage, String url) {
        final String url_Link = url;
        try {
            Log.d("system ", "textNowPlaying ::" + now);
            Log.d("system ", "textNextSong :: " + next);
            textNowPlaying.setText(now);
            textNameSong.setText(next);
            if (pathImage != null && !pathImage.equals(""))
                imageLoader.DisplayImage(MainActivity.path_Image_Cover_NowPlaying + pathImage,imageView3);
//                Glide.with(mainActivity).load(MainActivity.path_Image_Cover_NowPlaying + pathImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView3);
            else{
                imageView3.setImageResource(R.drawable.seed_head);
            }
            imageView3.setBackgroundResource(R.drawable.seed_head);

            if (url != null && !url.equals("")) {
                imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mainActivity, WebviewActivity.class);
                        intent.putExtra("URL", url_Link);
//                        Bundle bundle = ActivityOptions.makeCustomAnimation(mainActivity, R.anim.slide_in_up, R.anim.slide_out_up).toBundle();
                        mainActivity.startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                });
            }else{
                imageView3.setOnClickListener(null);
            }


            if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowLyric() != null && !mainActivity.getCurrentPlay().getNowLyric().equals(""))) {
                setDisableButtonLyric(true);
            } else if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowLyric() == null || mainActivity.getCurrentPlay().getNowLyric().equals(""))) {
                setDisableButtonLyric(false);
            }
            if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowMv() != null && !mainActivity.getCurrentPlay().getNowMv().equals(""))) {
                setDisableButtonMV(true);
            } else if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowMv() == null || mainActivity.getCurrentPlay().getNowMv().equals(""))) {
                setDisableButtonMV(false);
            }
        } catch (Exception ex) {

        }
    }

    public void updateListViewFragmentListPageFromFragmentMain() {
        if (fragmentListPage != null) {
            fragmentListPage.updateListView();
        }
    }

    public void playMedia() {
        if (play != null) {
            play.playMedia(false);
        }
    }

    public void sendEventClickPlay() {
        if (bt_play != null)
            bt_play.performClick();
    }

    public void pauseMedia() {
        if (play != null && play.returnIsPlating()) {
            play.force_pause();
        }
    }

    public void setDisableButtonMV(Boolean isEnable) {
        if (bt_youtube != null) {
            bt_youtube.setEnabled(isEnable);
        }
        if (!isEnable) {
            bt_youtube.setImageResource(R.drawable.lock_mv);
        } else {
            bt_youtube.setImageResource(R.drawable.unlock_mv);
        }
    }

    public void setDisableButtonList(Boolean isEnable) {
        if (bt_list != null) {
            bt_list.setEnabled(isEnable);
            if (!isEnable) {
//                bt_list.setBackgroundColor(Color.DKGRAY);
                bt_list.setImageResource(R.drawable.lock_list);
            }
        }
    }

    public void setDisableButtonLyric(Boolean isEnable) {
        if (bt_lyrics != null) {
            bt_lyrics.setEnabled(isEnable);
            if (!isEnable) {
                bt_lyrics.setImageResource(R.drawable.lock_lylics);
            } else {
                bt_lyrics.setImageResource(R.drawable.unlock_lylics);
            }

        }
    }

    public void setDjAdapter(){
        if(mainActivity!=null) {
            DJPageAdapter adapter = new DJPageAdapter(getChildFragmentManager(), mainActivity.getDJInfos());
            adapter.notifyDataSetChanged();
            AutoScrollViewPager pager = (AutoScrollViewPager) view.findViewById(R.id.pager);
            pager.setAdapter(adapter);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            nowPlaying = (TextView) view.findViewById(R.id.now_playing);
            TextView textNext = (TextView) view.findViewById(R.id.textNext);
            next = (TextView) view.findViewById(R.id.textView3);
            Log.d("system","State in Fragmentmain 1");
            mainActivity = (MainActivity) getActivity();
            mainActivity.setTypeFace(nowPlaying);
            mainActivity.setTypeFace(next);
            mainActivity.setTypeFace(textNext);

            DJPageAdapter adapter = new DJPageAdapter(getChildFragmentManager(), mainActivity.getDJInfos());
            AutoScrollViewPager pager = (AutoScrollViewPager) view.findViewById(R.id.pager);
            pager.setAdapter(adapter);
            if (mainActivity.getDJInfos().size() != 0 && mainActivity.getDJInfos().size() != 1) {
                pager.startAutoScroll();
                pager.setCycle(true);
                pager.setInterval(15000);
            }
            bt_youtube = (ImageButton) view.findViewById(R.id.bt_youtube);
            bt_lyrics = (ImageButton) view.findViewById(R.id.bt_lyrics);
            bt_play = (ImageButton) view.findViewById(R.id.bt_play);
            bt_play_load = (GifImageView) view.findViewById(R.id.bt_play_load);
            bt_list = (ImageButton) view.findViewById(R.id.bt_list);
            bt_mute = (ImageButton) view.findViewById(R.id.bt_mute);
            imageView3 = (ImageView) view.findViewById(R.id.imageView3);
            textNowPlaying = (TextView) view.findViewById(R.id.textNowPlaying);
            textNameSong = (TextView) view.findViewById(R.id.textNameSong);
            seekbar = (SeekBar) view.findViewById(R.id.seekBar);
            ImageView im_1 = (ImageView) view.findViewById(R.id.im_1);
            ImageView im_2 = (ImageView) view.findViewById(R.id.im_2);
            textNowPlaying.getBackground().setColorFilter(Color.parseColor("#4FFFFFFF"), PorterDuff.Mode.MULTIPLY);
            mainActivity.setTypeFaceOther(textNameSong);
            mainActivity.setTypeFaceOther(textNowPlaying);
            im_1.getDrawable().setColorFilter(Color.parseColor("#4FFFFFFF"), PorterDuff.Mode.MULTIPLY);
            im_2.getDrawable().setColorFilter(Color.parseColor("#4FFFFFFF"), PorterDuff.Mode.MULTIPLY);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;


            android.view.ViewGroup.LayoutParams layoutParamsnext = next.getLayoutParams();
            android.view.ViewGroup.LayoutParams layoutParamsim_1 = im_1.getLayoutParams();
            android.view.ViewGroup.LayoutParams layoutParamsim_2 = im_2.getLayoutParams();

            int sumW = layoutParamsnext.width + layoutParamsim_1.width + layoutParamsim_2.width;


            if(Build.VERSION.SDK_INT > 15) {
                textNowPlaying.setMaxWidth(width - sumW - (int)(width * 0.3));
            }else {
                android.view.ViewGroup.LayoutParams layouttextNowPlaying = textNowPlaying.getLayoutParams();
                layouttextNowPlaying.width = width - sumW  - (int)(width * 0.3);
                textNowPlaying.setLayoutParams(layouttextNowPlaying);
            }


            android.view.ViewGroup.LayoutParams layoutParams = imageView3.getLayoutParams();
            layoutParams.width = (int) (width*0.41);
            layoutParams.height =(int) (width*0.41);
            imageView3.setLayoutParams(layoutParams);
            ((ViewGroup.MarginLayoutParams) imageView3.getLayoutParams()).topMargin = layoutParams.height/2 - layoutParams.height/2/2;


            android.view.ViewGroup.LayoutParams layoutParamsbt_youtube = bt_youtube.getLayoutParams();
            layoutParamsbt_youtube.width = (int) (width*0.13);
            layoutParamsbt_youtube.height =(int) (width*0.13);
            bt_youtube.setLayoutParams(layoutParamsbt_youtube);
            ((ViewGroup.MarginLayoutParams) bt_youtube.getLayoutParams()).leftMargin = (((width/2) - (layoutParams.width/2))/2) - (layoutParamsbt_youtube.width/2);

            android.view.ViewGroup.LayoutParams layoutParamsbt_lyrics = bt_lyrics.getLayoutParams();
            layoutParamsbt_lyrics.width = (int) (width*0.13);
            layoutParamsbt_lyrics.height = (int) (width*0.13);
            bt_lyrics.setLayoutParams(layoutParamsbt_lyrics);
            ((ViewGroup.MarginLayoutParams) bt_lyrics.getLayoutParams()).leftMargin = (((width/2) - (layoutParams.width/2))/2) - (layoutParamsbt_youtube.width/2);

            android.view.ViewGroup.LayoutParams layoutParamsbt_list = bt_list.getLayoutParams();
            layoutParamsbt_list.width = (int) (width*0.13);
            layoutParamsbt_list.height = (int) (width*0.13);
            bt_list.setLayoutParams(layoutParamsbt_list);
            ((ViewGroup.MarginLayoutParams) bt_list.getLayoutParams()).leftMargin = (((width/2) - (layoutParams.width/2))/2) - (layoutParamsbt_youtube.width/2);

            android.view.ViewGroup.LayoutParams layoutParamsbt_play = bt_play.getLayoutParams();
            layoutParamsbt_play.width = (int) (width*0.13);
            layoutParamsbt_play.height = (int) (width*0.13);
            bt_play.setLayoutParams(layoutParamsbt_play);
            ((ViewGroup.MarginLayoutParams) bt_play.getLayoutParams()).rightMargin = (((width/2) - (layoutParams.width/2))/2) - (layoutParamsbt_youtube.width/2);

            android.view.ViewGroup.LayoutParams layoutParamsbt_play_load = bt_play_load.getLayoutParams();
            layoutParamsbt_play_load.width = (int) (width*0.13);
            layoutParamsbt_play_load.height = (int) (width*0.13);
            bt_play_load.setLayoutParams(layoutParamsbt_play_load);
            ((ViewGroup.MarginLayoutParams) bt_play_load.getLayoutParams()).rightMargin = (((width/2) - (layoutParams.width/2))/2) - (layoutParamsbt_youtube.width/2);
            audioManager = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
            Log.d("system", "Volume in fragmentmain 1 : " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            progressFromAudioManager = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {

                    seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                }
            };
            audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            seekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            textNameSong.setSelected(true);
            textNowPlaying.setSelected(true);


            if (audioManager != null && audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
            } else {
                bt_mute.setImageResource(R.drawable.speaker_mute_white);
            }


            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    bt_mute.setImageResource(R.drawable.speaker);

                    if (progress == 0) {
                        bt_mute.setImageResource(R.drawable.speaker_mute_white);
                    }
                    mainActivity.setSeekVal(progress);


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            seekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            mainActivity.setSeekBar(seekbar);
            mainActivity.setSeekVal(seekbar.getProgress());
            mainActivity.setBt_mute(bt_mute);
            mainActivity.setAudioManager(audioManager);

            bt_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (seekbar.getProgress() != 0) {
                        oldProgress = seekbar.getProgress();
                    }
                    if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                        seekbar.setProgress(0);
                        bt_mute.setImageResource(R.drawable.speaker_mute_white);
                    } else {
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                        bt_mute.setImageResource(R.drawable.speaker);
                        seekbar.setProgress(oldProgress);
                    }
                }
            });


            String now = "";
            String next = "";
            String pathImage = "";
            String url_Link = "";
            if (mainActivity != null) {
                if (mainActivity.getCurrentPlay() != null && mainActivity.getCurrentPlay().getEvent_type().equals("song")) {
                    now = (mainActivity.getCurrentPlay().getSongTitle() != null ? mainActivity.getCurrentPlay().getSongTitle() : "Seed 97.5 FM") + (mainActivity.getCurrentPlay().getArtistName() != null && mainActivity.getCurrentPlay().getArtistName() != "" ? " - " + mainActivity.getCurrentPlay().getArtistName() : "");
                    pathImage = mainActivity.getCurrentPlay().getSongCover() != null ? mainActivity.getCurrentPlay().getSongCover() : "";
                } else if (mainActivity.getCurrentPlay().getEvent_type().equals("spot")) {
                    now = (mainActivity.getCurrentPlay().getLink_title() != null ? mainActivity.getCurrentPlay().getLink_title() : mainActivity.getCurrentPlay().getLinkTitle()!=null ? mainActivity.getCurrentPlay().getLinkTitle() : "Seed 97.5 FM");
                    pathImage = mainActivity.getCurrentPlay().getLinkCover() != null ? mainActivity.getCurrentPlay().getLinkCover() : "";
                    url_Link = mainActivity.getCurrentPlay().getLinkUrl();
                }
                if (mainActivity.getNextPlay() != null && mainActivity.getNextPlay().getEvent_type().equals("song")) {
                    next = (mainActivity.getNextPlay().getSongTitle() != null ? mainActivity.getNextPlay().getSongTitle() : "Seed 97.5 FM") + (mainActivity.getNextPlay().getArtistName() != null && mainActivity.getNextPlay().getArtistName() != "" ? " - " + mainActivity.getNextPlay().getArtistName() : "");
                } else if (mainActivity.getNextPlay().getEvent_type().equals("spot")) {
                    next = (mainActivity.getNextPlay().getLink_title() != null ? mainActivity.getNextPlay().getLink_title() : mainActivity.getCurrentPlay().getLinkTitle()!=null ? mainActivity.getCurrentPlay().getLinkTitle() : "Seed 97.5 FM");
                }
                if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowLyric() != null && !mainActivity.getCurrentPlay().getNowLyric().equals(""))) {
                    setDisableButtonLyric(true);
                } else if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowLyric() == null || mainActivity.getCurrentPlay().getNowLyric().equals(""))) {
                    setDisableButtonLyric(false);
                }
                if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowMv() != null && !mainActivity.getCurrentPlay().getNowMv().equals(""))) {
                    setDisableButtonMV(true);
                } else if (mainActivity.getCurrentPlay() != null && (mainActivity.getCurrentPlay().getNowMv() == null || mainActivity.getCurrentPlay().getNowMv().equals(""))) {
                    setDisableButtonMV(false);
                }
            }

            updateNowPlayingAndNext(now, next, pathImage, url_Link);
//            updateNowPlayingAndNext(mainActivity.getCurrentPlay() != null && mainActivity.getCurrentPlay().getSongTitle() != null ? mainActivity.getCurrentPlay().getSongTitle() : "", mainActivity.getNextPlay() != null && mainActivity.getNextPlay().getSongTitle() != null ? mainActivity.getNextPlay().getSongTitle() : "");


            bt_youtube.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FragmentYouTube fragmentYouTube = new FragmentYouTube();
                    if (mainActivity.getCurrentPlay().getNowMv() != null) {
                        String url = mainActivity.getCurrentPlay().getNowMv();
                        String[] strings = url.split(mainActivity.getCutURLYoutube());

                        if (mainActivity.getCurrentPlay() != null && mainActivity.getCurrentPlay().getEvent_type().equals("song")) {
                            String name = (mainActivity.getCurrentPlay().getSongTitle()!=null ? mainActivity.getCurrentPlay().getSongTitle() :"Seed 97.5 FM");
                            fragmentYouTube.setMusicName(name + (mainActivity.getCurrentPlay().getArtistName() != null && mainActivity.getCurrentPlay().getArtistName() != "" ? " - " + mainActivity.getCurrentPlay().getArtistName() : ""));
                        }

                        if (strings.length > 0) {
                            fragmentYouTube.setYoutubeName(strings[strings.length - 1]);
                        }

                    }
                    mainActivity.setFragmentNoBack(fragmentYouTube);
                }
            });

            bt_lyrics.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FragmentLyrics fragmentLyrics = new FragmentLyrics();
                    if (mainActivity != null) {
                        if (mainActivity.getCurrentPlay() != null && mainActivity.getCurrentPlay().getEvent_type().equals("song")) {
                            Music music = new Music();
                            music.setLyrics(mainActivity.getCurrentPlay().getNowLyric());
                            music.setAuthor(mainActivity.getCurrentPlay().getNowAuthor());
                            music.setAuthor2(mainActivity.getCurrentPlay().getNowAuthor2());
                            music.setAuthor3(mainActivity.getCurrentPlay().getNowAuthor3());

                            if(mainActivity.getCurrentPlay().getNowAuthor() != null && mainActivity.getCurrentPlay().getNowAuthor() != "")
                                music.setName(mainActivity.getCurrentPlay().getSongTitle() + (mainActivity.getCurrentPlay().getArtistName() != null && mainActivity.getCurrentPlay().getArtistName() != "" ? " - " + mainActivity.getCurrentPlay().getArtistName() : "Seed 97.5 FM"));
                            else
                                music.setName(mainActivity.getCurrentPlay().getSongTitle() + (mainActivity.getCurrentPlay().getArtistName() != null && mainActivity.getCurrentPlay().getArtistName() != "" ? " - " + mainActivity.getCurrentPlay().getArtistName() : "Seed 97.5 FM"));
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

            if(play != null && play.getFlagStop()) {
                bt_play_load.setVisibility(View.VISIBLE);
                bt_play.setVisibility(View.GONE);
                play = null;
            }

            new PlayMediaTask().execute();

            if (play != null && play.returnIsPlating()) {
                bt_play.setImageResource(R.drawable.pause_button);
            }


        } catch (Exception e) {

        }
        if (bt_play.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.play_button).getConstantState())) {

            bt_play.callOnClick();
        }


        bt_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mainActivity.getFlagPause()) {
                    bt_play.setImageResource(R.drawable.pause_button);
                    mainActivity.setFlagPause(false);
                    if(play == null || play.getFlagStop()) {
                        bt_play_load.setVisibility(View.VISIBLE);
                        bt_play.setVisibility(View.GONE);
                        play = null;
                        new PlayMediaTask().execute();
                    }else{
                        play.playMedia(true);
                    }
                } else {
                    mainActivity.setFlagPause(true);
                    mainActivity.setForce_stop(Boolean.FALSE);
                    bt_play.setImageResource(R.drawable.play_button);
                    play.playMedia(false);
                }
            }
        });

        return view;
    }


    public void ss(){
        bt_youtube.setImageResource(R.drawable.unlock_list);
    }



    class PlayMediaTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            if (play == null  && !mainActivity.getFlagPause())
            play = new PlayMedia(mainActivity.getURL(), mainActivity.returnBaseContext(), mHandler, bt_play, bt_play_load, mainActivity);

            if(play != null) {
                play.setBt_play(bt_play);
                play.setBt_play_load(bt_play_load);
                mainActivity.setPlayMedia(play);
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(String file_url) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {

                    if (play != null && !play.returnIsPlating() && !mainActivity.getFlagPause()) {
                        bt_play.setImageResource(R.drawable.pause_button);
                        play.playStart();
                        mainActivity.setFlagPause(false);
                    } else {
                        bt_play.setVisibility(View.VISIBLE);
                        bt_play_load.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        mainActivity.setSeekVal(seekbar.getProgress());
        mainActivity.setSeekVal(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        flagClick = Boolean.TRUE;
        Log.d("system","Volume in fragmentmain  3 :");
        super.onDestroyView();
    }
}
