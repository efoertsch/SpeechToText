package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.record.AudioRecordActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


// Based on  https://google.github.io/dagger//android.html
@PerActivity
@Subcomponent(modules = {AudioRecordActivityModule.class, RealmModule.class})
public interface AudioRecordActivitySubComponent  extends AndroidInjector<AudioRecordActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AudioRecordActivity> {}



}
