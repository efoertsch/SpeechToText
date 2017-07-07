package com.fisincorporated.speechtotext.dagger.application;


import com.fisincorporated.speechtotext.application.AudioApplication;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.dagger.BuildersModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


@Singleton
@Component(modules = {BuildersModule.class, AndroidInjectionModule.class, ApplicationModule.class})
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(AudioApplication application);
        ApplicationComponent build();
    }

    void inject(AudioApplication audioApplication);

    AudioService getAudioService();

}



