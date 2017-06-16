package com.fisincorporated.speechtotext.dagger;

import android.app.Activity;

import com.fisincorporated.speechtotext.audio.list.AudioListActivity;
import com.fisincorporated.speechtotext.audio.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.audio.record.AudioRecordActivity;
import com.fisincorporated.speechtotext.dagger.activity.AudioListActivitySubComponent;
import com.fisincorporated.speechtotext.dagger.activity.AudioPlaybackActivitySubComponent;
import com.fisincorporated.speechtotext.dagger.activity.AudioRecordActivitySubComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {AudioListActivitySubComponent.class, AudioRecordActivitySubComponent.class, AudioPlaybackActivitySubComponent.class})
public abstract class BuildersModule {

    @Binds
    @IntoMap
    @ActivityKey(AudioListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindAudioListActvityInjectorFactory(AudioListActivitySubComponent.Builder builder);


    @Binds
    @IntoMap
    @ActivityKey(AudioRecordActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindAudioRecordActvityInjectorFactory(AudioRecordActivitySubComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(AudioPlaybackActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindAudioPlaybackActvityInjectorFactory(AudioPlaybackActivitySubComponent.Builder builder);

    // Add more bindings here for other sub components
    // Be sure not to provide any dependencies for the subcomponent here since this module will be included in the application component and could thereby have application scope.

}