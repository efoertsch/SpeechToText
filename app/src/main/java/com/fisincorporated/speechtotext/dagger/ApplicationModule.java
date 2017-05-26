package com.fisincorporated.speechtotext.dagger;

import android.app.Application;
import android.content.Context;

import com.fisincorporated.speechtotext.application.AudioApplication;
import com.fisincorporated.speechtotext.dagger.custom.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final AudioApplication application;

    public ApplicationModule(AudioApplication application) {
        this.application = application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context providesContext(){
        return application;
    }

}