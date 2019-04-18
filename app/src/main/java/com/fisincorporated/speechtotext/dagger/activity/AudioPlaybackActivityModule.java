package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AudioPlaybackActivityModule {

    @PerActivity
    @Provides
    public AudioPlaybackViewModel providesAudioPlaybackViewModel(AudioPlaybackActivity activity, @Singleton AudioRecordUtils audioRecordUtils) {
        return new AudioPlaybackViewModel(activity, audioRecordUtils);
    }

}
