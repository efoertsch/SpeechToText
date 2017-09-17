package com.fisincorporated.speechtotext.googlespeech.speechrequest;


import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// See https://cloud.google.com/speech/reference/rest/v1/speech/longrunningrecognize
public class LongRunningRecognize extends BaseJson {

    @SerializedName("config")
    @Expose
    private RecognitionConfig config;

    @SerializedName("audio")
    @Expose
    private RecognitionAudio audio;

    public RecognitionConfig getConfig() {
        return config;
    }

    public void setConfig(RecognitionConfig config) {
        this.config = config;
    }

    public RecognitionAudio getAudio() {
        return audio;
    }

    public void setAudio(RecognitionAudio audio) {
        this.audio = audio;
    }

}