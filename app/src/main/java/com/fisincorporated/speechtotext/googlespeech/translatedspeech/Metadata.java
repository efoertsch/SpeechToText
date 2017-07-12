package com.fisincorporated.speechtotext.googlespeech.translatedspeech;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("@type")
    @Expose
    private String type;
    @SerializedName("progressPercent")
    @Expose
    private Integer progressPercent;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("lastUpdateTime")
    @Expose
    private String lastUpdateTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Integer progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}