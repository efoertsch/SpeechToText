package com.fisincorporated.speechtotext.dagger.io;

import com.fisincorporated.speechtotext.retrofit.GoogleSpeechRetrofit;
import com.fisincorporated.speechtotext.retrofit.LoggingInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import retrofit2.Retrofit;

@Module
public class RetrofitModule {

    @Provides
    @Singleton
    public Retrofit provideGoogleSpeechRetrofit() {
        return new GoogleSpeechRetrofit(getInterceptor()).getRetrofit();
    }

    @Provides
    @Singleton
    public Interceptor getInterceptor() {
        return new LoggingInterceptor();
    }
}
