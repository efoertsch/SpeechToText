package com.fisincorporated.speechtotext.ui.record;

import android.os.Bundle;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.ui.AudioBaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class AudioRecordActivity extends AudioBaseActivity  implements FinishedRecordingCallback {

    private static final String LOG_TAG = AudioRecordActivity.class.getSimpleName();

    @Inject
    public AudioRecordViewModel audioRecordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Do this before super.onCreate in case the activity uses fragments
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_record);

        setupToolbar();
        toolbar.setTitle(R.string.audio_record_activity_title);

        audioRecordViewModel.setFinishedRecordingCallback(this);
        audioRecordViewModel.setView(findViewById(android.R.id.content));
    }

    @Override
    public void onBackPressed () {
        audioRecordViewModel.stopRecording();
    }


    @Override
    public void finishedRecording() {
        finish();
    }
}
