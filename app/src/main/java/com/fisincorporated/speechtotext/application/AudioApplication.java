package com.fisincorporated.speechtotext.application;

import android.util.Log;

import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.application.ApplicationComponent;
import com.fisincorporated.speechtotext.dagger.application.DaggerApplicationComponent;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AudioApplication extends DaggerApplication {
    private static final String TAG = AudioApplication.class.getSimpleName();
    protected ApplicationComponent applicationComponent;

//    @Inject
//    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
//
//    @Inject
//    DispatchingAndroidInjector<Service> dispatchingAndroidJobServiceInjector;


    @Inject
    AudioRecordUtils audioRecordsUtils;

    @Override
    public void onCreate() {
        connectRealm();
        super.onCreate();
        //connectRealm();
        applicationInjector();
        audioRecordsUtils.listAudioFiles();
        audioRecordsUtils.createMissingAudioRecords();
        loadAudioConverter();


    }

    private void connectRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("audio.files")
                .schemaVersion(0)
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(AudioRecord.class, new Long(0));
                    }
                })
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(AudioRecord.class).count();
        Log.d(TAG, "number of audio records:" + count);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        applicationComponent = DaggerApplicationComponent.builder().application(this).build();
        applicationComponent.inject(this);
        return applicationComponent;
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
