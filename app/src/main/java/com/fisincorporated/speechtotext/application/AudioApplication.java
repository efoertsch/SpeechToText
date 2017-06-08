package com.fisincorporated.speechtotext.application;

import android.app.Activity;
import android.app.Application;

import com.fisincorporated.speechtotext.audio.AudioRecord;
import com.fisincorporated.speechtotext.dagger.application.DaggerApplicationComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AudioApplication extends Application implements HasDispatchingActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        createDaggerInjections();
        configureRealm();
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

    private void configureRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("audio.files")
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(AudioRecord.class);
                    }
                })
                .build();
        //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
    }


}
