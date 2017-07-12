package com.fisincorporated.speechtotext.googlespeech.speechrequest;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecognitionConfig {
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("sampleRateHertz")
    @Expose
    private Integer sampleRateHertz;
    @SerializedName("languageCode")
    @Expose
    private String languageCode;
    @SerializedName("maxAlternatives")
    @Expose
    private Integer maxAlternatives;
    @SerializedName("profanityFilter")
    @Expose
    private Boolean profanityFilter;
    @SerializedName("speechContexts")
    @Expose
    private List<SpeechContext> speechContexts = null;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Integer getSampleRateHertz() {
        return sampleRateHertz;
    }

    public void setSampleRateHertz(Integer sampleRateHertz) {
        this.sampleRateHertz = sampleRateHertz;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Integer getMaxAlternatives() {
        return maxAlternatives;
    }

    public void setMaxAlternatives(Integer maxAlternatives) {
        this.maxAlternatives = maxAlternatives;
    }

    public Boolean getProfanityFilter() {
        return profanityFilter;
    }

    public void setProfanityFilter(Boolean profanityFilter) {
        this.profanityFilter = profanityFilter;
    }

    public List<SpeechContext> getSpeechContexts() {
        return speechContexts;
    }

    public void setSpeechContexts(List<SpeechContext> speechContexts) {
        this.speechContexts = speechContexts;
    }

}
