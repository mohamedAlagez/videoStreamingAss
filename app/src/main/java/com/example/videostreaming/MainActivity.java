package com.example.videostreaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class MainActivity extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer player;
    String videoURL;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playPackPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.video_view);
        Intent i = getIntent();
        videoURL = i.getStringExtra("uri");

    }

    public void initVideo(String videoURL) {
        player = ExoPlayerFactory.newSimpleInstance(this);

        playerView.setPlayer(player);

        Uri uri = Uri.parse(videoURL);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "exoplayer.codelab");
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playPackPosition);
        player.prepare(mediaSource, false, false);

    }

    public void releaseVideo() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playPackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    ;

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        videoURL = i.getStringExtra("uri");

        initVideo(videoURL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        videoURL = i.getStringExtra("uri");

        if (player != null) {
            initVideo(videoURL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseVideo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseVideo();
    }
}