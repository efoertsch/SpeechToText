package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@PerActivity
@Module
public class AudioPlaybackActivityModule {

    @Provides
    public AudioPlaybackViewModel providesAudioPlaybackViewModel(AudioPlaybackActivity activity, @Singleton AudioService audioService, @PerActivity Realm realm) {
        return new AudioPlaybackViewModel(activity, audioService, realm);
    }


}
