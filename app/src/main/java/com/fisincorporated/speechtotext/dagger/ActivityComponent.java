package com.fisincorporated.speechtotext.dagger;

import com.fisincorporated.speechtotext.audio.AudioRecorderActivity;
import com.fisincorporated.speechtotext.dagger.custom.PerActivity;

import dagger.Component;


@PerActivity
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(AudioRecorderActivity activity);

}