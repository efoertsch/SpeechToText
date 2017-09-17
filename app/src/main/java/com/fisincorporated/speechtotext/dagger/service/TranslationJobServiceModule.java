package com.fisincorporated.speechtotext.dagger.service;


import android.content.Context;

import com.fisincorporated.speechtotext.audio.utils.SpeechToTextService;
import com.fisincorporated.speechtotext.dagger.annotations.PerService;

import dagger.Module;
import dagger.Provides;

@PerService
@Module
public class TranslationJobServiceModule {
    @Provides
    public SpeechToTextService providesSpeechToTextService(Context context) {
        return new SpeechToTextService(context);
    }


}