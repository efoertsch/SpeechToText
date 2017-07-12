package com.fisincorporated.speechtotext.googlespeech.translatedspeech;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alternative {

    @SerializedName("transcript")
    @Expose
    private String transcript;
    @SerializedName("confidence")
    @Expose
    private Double confidence;

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

}
