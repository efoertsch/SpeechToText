package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AudioPlaybackActivityModule {

    @Provides
    public AudioPlaybackViewModel providesAudioPlaybackViewModel(AudioPlaybackActivity activity, @Singleton AudioService audioService) {
        return new AudioPlaybackViewModel(activity, audioService);
    }


}
