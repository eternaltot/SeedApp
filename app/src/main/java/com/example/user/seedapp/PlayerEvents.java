package com.example.user.seedapp;

/**
 * Created by TOT on 13/5/2558.
 */

public interface PlayerEvents {
    public void onStart(String mime, int sampleRate, int channels, long duration);

    public void onPlay();

    public void onPlayUpdate(int percent, long currentms, long totalms);

    public void onStop();

    public void onError();
}

