package com.fisincorporated.speechtotext.dagger.service;

import com.fisincorporated.speechtotext.dagger.activity.RealmModule;
import com.fisincorporated.speechtotext.dagger.annotations.PerService;
import com.fisincorporated.speechtotext.jobscheduler.TranslationJobService;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerService
@Subcomponent(modules= {RealmModule.class})
public interface TranslationJobServiceSubcomponent extends AndroidInjector<TranslationJobService> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TranslationJobService> {
    }
}
