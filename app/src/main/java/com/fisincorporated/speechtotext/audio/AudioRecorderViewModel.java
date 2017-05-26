package com.fisincorporated.speechtotext.audio;


import android.content.ContextWrapper;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.application.ViewModelLifeCycle;
import com.fisincorporated.speechtotext.dagger.custom.ActivityContext;
import com.fisincorporated.speechtotext.databinding.AudioRecordingBinding;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;

@ActivityContext
public class AudioRecorderViewModel implements ViewModelLifeCycle {

    private File audioDirectory;

    private View bindingView;

    private AudioRecordingBinding viewDataBinding;

    private AudioRecord audioRecord;

    @Inject
    public AudioService audioService;

    @Inject
    public AudioRecordAdapter audioRecordAdapter;

    @Inject
    public AudioRecorderViewModel() {
    }

    public AudioRecorderViewModel setView(View view) {

        bindingView = view.findViewById(R.id.activity_recorder_layout);
        viewDataBinding = DataBindingUtil.bind(bindingView);
        setupRecyclerView(viewDataBinding.activityRecorderRecyclerView);
        viewDataBinding.setViewmodel(this);

        // Record to the external cache directory for visibility
        audioDirectory = (new ContextWrapper(view.getContext())).getFilesDir();

        FloatingActionButton fab = viewDataBinding.activityRecorderMicrophoneFab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return this;
    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    private void onRecord(boolean start) {
        if (start) {
            String audioFileName = UUID.randomUUID().toString() + ".3gp";
            audioRecord = createAudioRecord(new Date(), audioFileName);
            audioService.startRecording(audioFileName);
        } else {
            audioService.stopRecording();
        }
    }

    private AudioRecord createAudioRecord(Date currentDate, String audioFileName) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        AudioRecord audioRecord = realm.createObject(AudioRecord.class);
        audioRecord.setMillisecId(currentDate.getTime());
        audioRecord.setAudioFileName(audioDirectory + audioFileName);
        realm.commitTransaction();
        return audioRecord;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        audioService.stopPlaying();
        audioService.stopRecording();
    }

}
