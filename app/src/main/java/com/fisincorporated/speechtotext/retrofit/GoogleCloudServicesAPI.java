package com.fisincorporated.speechtotext.retrofit;


import com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionConfig;
import com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognizeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleCloudServicesAPI {

    //POST https://speech.googleapis.com/v1/speech:longrunningrecognize?key=YOUR_API_KEY
    @POST(":longrunningrecognize")
    Call<RecognizeResponse> callSpeechToTextApi(@Query("key") String apiKey, @Body RecognitionConfig recognitionConfig);


}
