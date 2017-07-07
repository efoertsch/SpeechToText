package com.fisincorporated.speechtotext.application;

import android.app.Activity;
import android.app.Application;

import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.application.DaggerApplicationComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;

public class AudioApplication extends Application implements HasDispatchingActivityInjector {
    private static final String TAG = AudioApplication.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject AudioRecordUtils audioRecordsUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        createDaggerInjections();
        audioRecordsUtils.listAudioFiles();
        audioRecordsUtils.createMissingAudioRecords();
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

}
