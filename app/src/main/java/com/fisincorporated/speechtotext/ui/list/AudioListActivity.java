package com.fisincorporated.speechtotext.ui.list;

import android.os.Bundle;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.ui.AudioBaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class AudioListActivity extends AudioBaseActivity {

    private static final String LOG_TAG = AudioListActivity.class.getSimpleName();

    @Inject
    public AudioListViewModel audioListViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Do this before super.onCreate in case the activity uses fragments
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_list);

        setupToolbar();
        toolbar.setTitle(R.string.audio_list_activity_title);

        audioListViewModel.setView(findViewById(android.R.id.content));
    }



}