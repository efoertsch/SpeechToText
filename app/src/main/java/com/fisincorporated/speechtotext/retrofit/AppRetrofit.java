package com.fisincorporated.speechtotext.retrofit;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class AppRetrofit {

    public static final String GOOLGLE_SPEECH_URL = "https://speech.googleapis.com/v1/speech/";

    private Retrofit retrofit;

    @Inject
    public AppRetrofit(Interceptor interceptor){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GOOLGLE_SPEECH_URL)
                .client(new OkHttpClient());

        retrofit = builder.client(httpClient.build()).build();
    };

    public Retrofit getRetrofit() {
        return retrofit;
    }



}