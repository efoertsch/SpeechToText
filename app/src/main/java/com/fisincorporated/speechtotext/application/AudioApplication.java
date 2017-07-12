package com.fisincorporated.speechtotext.application;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.application.DaggerApplicationComponent;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;

public class AudioApplication extends Application implements HasDispatchingActivityInjector {
    private static final String TAG = AudioApplication.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject AudioRecordUtils audioRecordsUtils;

    private StorageReference storageRef;

    @Override
    public void onCreate() {
        super.onCreate();
        createDaggerInjections();
        audioRecordsUtils.listAudioFiles();
        audioRecordsUtils.createMissingAudioRecords();
        storageRef = FirebaseStorage.getInstance().getReference();
        loadAudioConverter();
    }

    protected void createDaggerInjections() {
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    private void loadAudioConverter() {
        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "AndroidAudioConverger loaded successfully");
            }

            @Override
            public void onFailure(Exception error) {
                Log.d(TAG, "AndroidAudioConverter NOT LOADED!");
            }
        });
    }


}
