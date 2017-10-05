package com.fisincorporated.speechtotext.ui.playback;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.ui.AudioBaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class AudioPlaybackActivity extends AudioBaseActivity {

    private static final String LOG_TAG = AudioPlaybackActivity.class.getSimpleName();

    public static final String AUDIO_ID = "AUDIO_ID";

    public boolean firstTime = true;

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
        viewModel.setAudioRecord(getIntent().getLongExtra(AUDIO_ID, 0));

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.translation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_re_run_translation:
                startTranslation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onPause();
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


    private void startTranslation() {
        viewModel.translateToText();
    }




}
