package com.fisincorporated.speechtotext.dagger.service;

import com.fisincorporated.speechtotext.dagger.activity.RealmModule;
import com.fisincorporated.speechtotext.dagger.io.RetrofitModule;
import com.fisincorporated.speechtotext.jobscheduler.TranslationJobService;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules= {TranslationJobServiceModule.class, RealmModule.class, RetrofitModule.class})
public interface TranslationJobServiceComponent {

    void inject(TranslationJobService translationJobService);

}
