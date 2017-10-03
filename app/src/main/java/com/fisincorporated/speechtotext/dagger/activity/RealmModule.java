package com.fisincorporated.speechtotext.dagger.activity;


import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

// reference https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
@Module
public class RealmModule {

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }


}
