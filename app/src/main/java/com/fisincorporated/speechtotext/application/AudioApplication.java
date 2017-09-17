package com.fisincorporated.speechtotext.application;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.application.DaggerApplicationComponent;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AudioApplication extends Application
//public class AudioApplication extends Application
        implements HasDispatchingActivityInjector {
    private static final String TAG = AudioApplication.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    AudioRecordUtils audioRecordsUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        connectRealm();
        createDaggerInjections();
        audioRecordsUtils.listAudioFiles();
        audioRecordsUtils.createMissingAudioRecords();
        loadAudioConverter();


    }

    private void connectRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("audio.files")
                .schemaVersion(0)
                //.migration(new AudioRecordMigration())
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(AudioRecord.class, new Long(0));
                    }
                })
                .build();
        //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(AudioRecord.class).count();
        Log.d(TAG, "number of audio records:" + count);
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
