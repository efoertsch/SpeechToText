package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.audio.record.AudioRecordActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


// Based on  https://google.github.io/dagger//android.html
@Subcomponent(modules = {AudioRecordActivityModule.class})
public interface AudioRecordActivitySubComponent  extends AndroidInjector<AudioRecordActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AudioRecordActivity> {}



}
