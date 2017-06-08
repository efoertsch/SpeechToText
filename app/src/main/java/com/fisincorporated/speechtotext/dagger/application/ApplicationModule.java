package com.fisincorporated.speechtotext.dagger.application;

import android.content.Context;

import com.fisincorporated.speechtotext.application.AudioApplication;
import com.fisincorporated.speechtotext.dagger.activity.AudioRecordActivitySubComponent;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides Context provideContext(AudioApplication application) {
        return application.getApplicationContext();
    }


}