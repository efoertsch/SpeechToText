package com.fisincorporated.speechtotext.dagger.activity;

import android.app.Activity;

import com.fisincorporated.speechtotext.ui.list.AudioListActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.ui.record.AudioRecordActivity;
import com.fisincorporated.speechtotext.ui.signin.SignInActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {AudioListActivitySubComponent.class, AudioRecordActivitySubComponent.class, AudioPlaybackActivitySubComponent.class, SignInActivitySubComponent.class})
public abstract class ActivityBuildersModule {

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

    @Binds
    @IntoMap
    @ActivityKey(SignInActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindSignInActvityInjectorFactory(SignInActivitySubComponent.Builder builder);

    // Add more bindings here for other sub components
    // Be sure not to provide any dependencies for the subcomponent here since this module will be included in the application applicationComponent and could thereby have application scope.

}