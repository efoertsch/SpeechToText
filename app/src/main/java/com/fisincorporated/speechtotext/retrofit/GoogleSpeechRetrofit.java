package com.fisincorporated.speechtotext.retrofit;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleSpeechRetrofit {

    public static final String GOOLGLE_SPEECH_URL = "https://speech.googleapis.com/";

    private Retrofit retrofit;

    @Inject
    public GoogleSpeechRetrofit(Interceptor interceptor){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (interceptor != null) {
            httpClient.addInterceptor(interceptor);
        }
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GOOLGLE_SPEECH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        retrofit = builder.build();
    };

    public Retrofit getRetrofit() {
        return retrofit;
    }


}