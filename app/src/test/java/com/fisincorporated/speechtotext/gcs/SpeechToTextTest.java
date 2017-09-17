package com.fisincorporated.speechtotext.gcs;

import com.fisincorporated.speechtotext.googlespeech.speechrequest.LongRunningRecognize;
import com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionAudio;
import com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionConfig;
import com.fisincorporated.speechtotext.googlespeech.speechresponse.OperationResponse;
import com.fisincorporated.speechtotext.retrofit.GoogleSpeechRetrofit;
import com.fisincorporated.speechtotext.retrofit.GoogleSpeechServicesApi;
import com.fisincorporated.speechtotext.retrofit.LoggingInterceptor;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;



public class SpeechToTextTest {

    @Test
    public void shouldStartSpeechToTextConverstion() throws IOException {
        Retrofit retrofit = new GoogleSpeechRetrofit(new LoggingInterceptor()).getRetrofit();
        GoogleSpeechServicesApi client = retrofit.create(GoogleSpeechServicesApi.class);

        final Call<OperationResponse> call;

        RecognitionConfig recognitionConfig = new RecognitionConfig();
        recognitionConfig.setEncoding(RecognitionConfig.AudioEncoding.FLAC.name());
        // TODO - 16000 may not apply for 3gp to flac converted file
        recognitionConfig.setSampleRateHertz(8000);
        // TODO set up option for encoding
        recognitionConfig.setLanguageCode("en-US");
        recognitionConfig.setMaxAlternatives(2);
        recognitionConfig.setProfanityFilter(true);
        // TODO - set up entry screen for list of hint text/phrases
        //recognitionConfig.setSpeechContexts();

        RecognitionAudio recognitionAudio = new RecognitionAudio();
        recognitionAudio.setUri("gs://speechtotext-f653f.appspot.com/audio/917ba572-2633-42bf-96ee-6eab04858361.flac");

        LongRunningRecognize longRunningRecognize = new LongRunningRecognize();
        longRunningRecognize.setConfig(recognitionConfig);
        longRunningRecognize.setAudio(recognitionAudio);

        call = client.callSpeechToTextApi("put in bearer token", longRunningRecognize);

        //AIzaSyAlMsCm7goQVAJa3UVSYKZOg4KqeHNqA6c
        //call = client.callSpeechToTextApiKeyOnly("AIzaSyAlMsCm7goQVAJa3UVSYKZOg4KqeHNqA6c", longRunningRecognize);

        OperationResponse response = call.execute().body();

        System.out.println(response.toString());

    }

}
