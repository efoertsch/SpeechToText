package com.fisincorporated.speechtotext.dagger.application;

import android.content.Context;

import com.fisincorporated.speechtotext.application.AudioApplication;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.retrofit.AppRetrofit;
import com.fisincorporated.speechtotext.retrofit.LoggingInterceptor;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import retrofit2.Retrofit;

@Module
public class ApplicationModule {

    @Provides Context provideContext(AudioApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    AudioService provideAudioService(){
        return new AudioService();
    }

    @Provides
    @Singleton
    public AudioRecordUtils providesAudioRecordUtils(AudioApplication application) {
        return new AudioRecordUtils(application);
    }

    @Provides
    @Singleton
    public StorageReference providesStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }

    @Provides
    @Singleton
    public Retrofit provideAppRetrofit() {
        return new AppRetrofit(getInterceptor()).getRetrofit();
    }

    @Provides
    @Singleton
    public Interceptor getInterceptor() {
        return new LoggingInterceptor();
    }


}