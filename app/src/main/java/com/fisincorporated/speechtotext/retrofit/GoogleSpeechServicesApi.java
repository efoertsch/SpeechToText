package com.fisincorporated.speechtotext.retrofit;


import com.fisincorporated.speechtotext.googlespeech.speechrequest.LongRunningRecognize;
import com.fisincorporated.speechtotext.googlespeech.speechresponse.OperationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GoogleSpeechServicesApi {

    /**
     * Start longrunningrecognize
     * https://cloud.google.com/speech/docs/async-recognize
     * @param bearerToken
     * @param longRunningRecognize
     * @return
     */


    @Headers("Content-Type:application/json")
    @POST("v1/speech:longrunningrecognize")
    Call<OperationResponse> callSpeechToTextApi(@Header("Authorization") String bearerToken, @Body LongRunningRecognize longRunningRecognize);


    /** Get status of longrunningrecognize operation
     * https://cloud.google.com/speech/docs/async-recognize
     * @param bearerToken
     * @param name - value from "name" field of Operation(aka OperationResponse) with successful response from longrunningrecognize api
     * @return  (aka Operation) speech to text process completed when "done" = true;
     */

    @Headers("Content-Type:application/json")
    @GET("v1/operations/{name}")
    Call<OperationResponse> callLongRunningRecognizeProgress(@Header("Authorization") String bearerToken,@Path(value = "name", encoded = true) String name);




}
