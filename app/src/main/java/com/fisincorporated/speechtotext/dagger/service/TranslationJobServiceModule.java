package com.fisincorporated.speechtotext.dagger.service;


import android.content.Context;

import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextService;
import com.fisincorporated.speechtotext.jobscheduler.TranslationJobService;
import com.fisincorporated.speechtotext.retrofit.GoogleSpeechRetrofit;

import dagger.Module;
import dagger.Provides;

@Module
public class TranslationJobServiceModule {

    TranslationJobService translationJobService;

    public TranslationJobServiceModule(TranslationJobService translationJobService){
        this.translationJobService = translationJobService;
    }

    @Provides
    Context providesContext(){
        return translationJobService.getBaseContext();

    }

    @Provides
    public SpeechToTextService providesSpeechToTextService(GoogleSpeechRetrofit googleSpeechRetrofit, AudioRecordUtils audioRecordUtils) {
        return new SpeechToTextService(providesContext() , googleSpeechRetrofit, audioRecordUtils);
    }

}