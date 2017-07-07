package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.list.AudioListActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

// Based on  https://google.github.io/dagger//android.html
@PerActivity
@Subcomponent(modules = {AudioListActivityModule.class, RealmModule.class})
public interface AudioListActivitySubComponent extends AndroidInjector<AudioListActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AudioListActivity> {}



}
