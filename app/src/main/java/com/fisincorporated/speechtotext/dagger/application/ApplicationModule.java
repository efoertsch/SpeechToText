package com.fisincorporated.speechtotext.dagger.application;

import android.content.Context;

import com.fisincorporated.speechtotext.application.AudioApplication;
import com.fisincorporated.speechtotext.audio.AudioService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides Context provideContext(AudioApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    AudioService provideAudioService(){
        return new AudioService();
    }


}