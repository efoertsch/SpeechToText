package com.fisincorporated.speechtotext.application;

import android.app.Application;

import com.fisincorporated.speechtotext.audio.AudioRecord;
import com.fisincorporated.speechtotext.dagger.ApplicationComponent;
import com.fisincorporated.speechtotext.dagger.ApplicationModule;
import com.fisincorporated.speechtotext.dagger.DaggerApplicationComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AudioApplication extends Application {

    protected ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        createDaggerInjections();
        configureRealm();
    }

    protected void createDaggerInjections() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {

        return component;
    }

    private void configureRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
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
