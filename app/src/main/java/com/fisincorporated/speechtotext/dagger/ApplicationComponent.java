package com.fisincorporated.speechtotext.dagger;


import android.app.Application;
import android.content.Context;

import com.fisincorporated.speechtotext.dagger.custom.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();
}


