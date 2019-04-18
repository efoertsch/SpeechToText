package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.record.AudioRecordActivity;
import com.fisincorporated.speechtotext.ui.record.AudioRecordViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AudioRecordActivityModule {

    @PerActivity
    @Provides
    public AudioRecordViewModel providesAudioRecordViewModel(AudioRecordActivity activity, @Singleton AudioService audioService, @Singleton AudioRecordUtils audioRecordUtils) {
        return new AudioRecordViewModel(activity, audioService, audioRecordUtils);
    }
}
