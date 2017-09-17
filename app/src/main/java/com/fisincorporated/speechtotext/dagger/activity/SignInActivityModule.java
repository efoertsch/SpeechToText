package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.audio.utils.SpeechToTextService;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.signin.SignInActivity;

import dagger.Module;
import dagger.Provides;


@PerActivity
@Module
public class SignInActivityModule {

    @Provides
    public SpeechToTextService providesSpeechToTextService(SignInActivity activity) {
        return new SpeechToTextService(activity);
    }


}