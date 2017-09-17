package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.signin.SignInActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = {SignInActivityModule.class, RealmModule.class})
public interface SignInActivitySubComponent extends AndroidInjector<SignInActivity>{

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<SignInActivity> {
    }
}

