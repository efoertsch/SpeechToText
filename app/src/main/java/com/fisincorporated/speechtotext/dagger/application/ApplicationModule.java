package com.fisincorporated.speechtotext.dagger.application;

import android.content.Context;

import com.fisincorporated.speechtotext.application.AudioApplication;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.retrofit.GcsRetrofit;
import com.fisincorporated.speechtotext.retrofit.GoogleSpeechRetrofit;
import com.fisincorporated.speechtotext.retrofit.LoggingInterceptor;

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
    public AudioRecordUtils providesAudioRecordUtils(AudioApplication application) {
        return new AudioRecordUtils(application);
    }

//    @Provides
//    @Singleton
//    public StorageReference providesStorageReference() {
//        return FirebaseStorage.getInstance().getReference();
//    }

    @Provides
    @Singleton
    public Retrofit provideGcsRetrofit() {
        return new GcsRetrofit(getInterceptor()).getRetrofit();
    }

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