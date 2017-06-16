package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.audio.playback.AudioPlaybackActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@Subcomponent(modules = {AudioPlaybackActivityModule.class})
public interface AudioPlaybackActivitySubComponent extends AndroidInjector<AudioPlaybackActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AudioPlaybackActivity> {}

}
