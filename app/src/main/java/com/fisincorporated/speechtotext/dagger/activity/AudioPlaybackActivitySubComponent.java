package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@PerActivity
@Subcomponent(modules = {AudioPlaybackActivityModule.class, RealmModule.class})
public interface AudioPlaybackActivitySubComponent extends AndroidInjector<AudioPlaybackActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AudioPlaybackActivity> {}

}
