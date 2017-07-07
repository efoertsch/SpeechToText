package com.fisincorporated.speechtotext.ui.playback;

import android.os.Bundle;
import android.view.MenuItem;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.ui.AudioBaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class AudioPlaybackActivity extends AudioBaseActivity {

    private static final String LOG_TAG = AudioPlaybackActivity.class.getSimpleName();

    public static final String AUDIO_ID = "AUDIO_ID";

    @Inject
    public AudioPlaybackViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Do this before super.onCreate in case the activity uses fragments
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_playback_layout);
        setupToolbar();
        toolbar.setTitle(R.string.audio_playback_activity_title);

        viewModel.setView(findViewById(android.R.id.content));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.playAudio(getIntent().getLongExtra(AUDIO_ID, 0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        viewModel.stopPlaying();
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        viewModel.onDestroy();
    }

}
