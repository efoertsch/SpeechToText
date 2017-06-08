package com.fisincorporated.speechtotext.dagger;

import android.app.Activity;

import com.fisincorporated.speechtotext.audio.AudioRecordActivity;
import com.fisincorporated.speechtotext.dagger.activity.AudioRecordActivitySubComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = AudioRecordActivitySubComponent.class)
public abstract class BuildersModule {

    @Binds
    @IntoMap
    @ActivityKey(AudioRecordActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindAudioRecordActvityInjectorFactory(AudioRecordActivitySubComponent.Builder builder);


    // Add more bindings here for other sub components
    // Be sure not to provide any dependencies for the subcomponent here since this module will be included in the application component and could thereby have application scope.

}