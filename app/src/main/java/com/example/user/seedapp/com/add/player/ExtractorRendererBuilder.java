/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.user.seedapp.com.add.player;

import android.media.MediaCodec;
import android.net.Uri;
import android.widget.TextView;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

/**
 * A {@link com.example.user.seedapp.com.add.player.DemoPlayer.RendererBuilder} for streams that can be read using an {@link com.google.android.exoplayer.extractor.Extractor}.
 */
public class ExtractorRendererBuilder implements DemoPlayer.RendererBuilder {

  private static final int BUFFER_SIZE = 10 * 1024 * 1024;

  private final String userAgent;
  private final Uri uri;
  private final TextView debugTextView;
  private final Extractor extractor;

  public ExtractorRendererBuilder(String userAgent, Uri uri, TextView debugTextView,
      Extractor extractor) {
    this.userAgent = userAgent;
    this.uri = uri;
    this.debugTextView = debugTextView;
    this.extractor = extractor;
  }

  @Override
  public void buildRenderers(DemoPlayer player, DemoPlayer.RendererBuilderCallback callback) {
    // Build the video and audio renderers.
    DataSource dataSource = new DefaultUriDataSource(userAgent, null);
    ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri, dataSource, extractor, 2,
        BUFFER_SIZE);
    MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(sampleSource,
        null, true, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5000, null, player.getMainHandler(),
        player, 50);
    MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource,
        null, true, player.getMainHandler(), player);

    // Build the debug renderer.
    TrackRenderer debugRenderer = debugTextView != null
        ? new DebugTrackRenderer(debugTextView, player, videoRenderer) : null;

    // Invoke the callback.
    TrackRenderer[] renderers = new TrackRenderer[DemoPlayer.RENDERER_COUNT];
    renderers[DemoPlayer.TYPE_VIDEO] = videoRenderer;
    renderers[DemoPlayer.TYPE_AUDIO] = audioRenderer;
    renderers[DemoPlayer.TYPE_DEBUG] = debugRenderer;
    callback.onRenderers(null, null, renderers);
  }

}
