package com.fisincorporated.speechtotext.googlespeech.speechrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// See https://cloud.google.com/speech/reference/rest/v1/RecognitionConfig#SpeechContext
public class SpeechContext {@SerializedName("phrases")
@Expose
private List<String> phrases = null;

    public List<String> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }

}