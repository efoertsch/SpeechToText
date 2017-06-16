package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.list.AudioListActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

// Based on  https://google.github.io/dagger//android.html
@Subcomponent(modules = {AudioListActivityModule.class})
public interface AudioListActivitySubComponent extends AndroidInjector<AudioListActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AudioListActivity> {}



}
