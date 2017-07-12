package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextService;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackViewModel;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
public class AudioPlaybackActivityModule {

    @Provides
    public AudioPlaybackViewModel providesAudioPlaybackViewModel(AudioPlaybackActivity activity, @Singleton AudioService audioService, @Singleton AudioRecordUtils audioRecordUtils, @PerActivity SpeechToTextService speechToTextService) {
        return new AudioPlaybackViewModel(activity, audioService, audioRecordUtils, speechToTextService);
    }

    @Provides
    public SpeechToTextService providesSpeechToTextService(AudioPlaybackActivity activity, @Singleton StorageReference storageReference) {
        return new SpeechToTextService(activity, storageReference);
    }



}
