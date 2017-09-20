package com.fisincorporated.speechtotext.retrofit;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GcsRetrofit {

    public static final String GCS_URL = "https://www.googleapis.com/";

    private Retrofit retrofit;

    @Inject
    public GcsRetrofit(Interceptor interceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (interceptor != null) {
            httpClient.addInterceptor(interceptor);
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GCS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        retrofit = builder.build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}


