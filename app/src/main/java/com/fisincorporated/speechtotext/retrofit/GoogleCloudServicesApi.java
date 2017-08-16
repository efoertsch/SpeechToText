package com.fisincorporated.speechtotext.retrofit;


import com.fisincorporated.speechtotext.googlespeech.speechrequest.LongRunningRecognize;
import com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognizeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleCloudServicesApi {

    //POST https://speech.googleapis.com/v1/speech:longrunningrecognize?key=YOUR_API_KEY
    @Headers("Content-Type:application/json")
    @POST("v1/speech:longrunningrecognize")
    Call<RecognizeResponse> callSpeechToTextApi(@Query("key") String apiKey, @Header("X-Android-Cert") String sha1, @Header("X-Android-Package") String appPackageName, @Body LongRunningRecognize longRunningRecognize);


    @Headers("Content-Type:application/json")
    @POST("v1/speech:longrunningrecognize")
    Call<RecognizeResponse> callSpeechToTextApiKeyOnly(@Query("key") String apiKey, @Body LongRunningRecognize longRunningRecognize);


}
