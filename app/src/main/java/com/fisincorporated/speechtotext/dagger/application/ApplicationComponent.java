package com.fisincorporated.speechtotext.dagger.application;


import com.fisincorporated.speechtotext.application.AudioApplication;
import com.fisincorporated.speechtotext.dagger.activity.ActivityBuildersModule;
import com.fisincorporated.speechtotext.dagger.io.RetrofitModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;


@Singleton
@Component(modules = {ActivityBuildersModule.class, AndroidInjectionModule.class, ApplicationModule.class, RetrofitModule.class
        , ChannelIdModule.class})
public interface ApplicationComponent extends AndroidInjector<AudioApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(AudioApplication application);

        ApplicationComponent build();
    }

    void inject(AudioApplication audioApplication);

}



