package com.fisincorporated.speechtotext.dagger.activity;

import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.jobscheduler.JobSchedulerUtil;
import com.fisincorporated.speechtotext.ui.signin.SignInActivity;

import dagger.Module;
import dagger.Provides;



@Module
public class SignInActivityModule {

    @PerActivity
    @Provides
    public JobSchedulerUtil providesJobSchedulerUtil(SignInActivity activity) {
        return new JobSchedulerUtil();
    }


}