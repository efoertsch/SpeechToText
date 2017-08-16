package com.fisincorporated.speechtotext.googlespeech.speechrequest;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// See https://cloud.google.com/speech/reference/rest/v1/RecognitionConfig
public class RecognitionConfig {

    // see https://cloud.google.com/speech/reference/rest/v1/RecognitionConfig#AudioEncoding
    public enum AudioEncoding {
        ENCODING_UNSPECIFIED,
        LINEAR16,
        FLAC,
        MULAW,
        AMR,
        AMR_WB,
        OGG_OPUS,
        SPEEX_WITH_HEADER_BYTE;
    }
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
