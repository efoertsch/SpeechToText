package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.record.AudioRecordActivity;
import com.fisincorporated.speechtotext.ui.record.AudioRecordViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@PerActivity
@Module
public class AudioRecordActivityModule {

    @Provides
    public AudioRecordViewModel providesAudioRecordViewModel(AudioRecordActivity activity, @Singleton AudioService audioService, @PerActivity Realm realm) {
        return new AudioRecordViewModel(activity, audioService, realm);
    }
}
