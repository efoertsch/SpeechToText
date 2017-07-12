package com.fisincorporated.speechtotext.googlespeech.translatedspeech;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("@type")
    @Expose
    private String type;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}