package com.fisincorporated.speechtotext.googlespeech.speechresponse;

import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.grpc.Metadata;

public class OperationResponse extends BaseJson {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    @SerializedName("done")
    @Expose
    private Boolean done;

    @SerializedName("error")
    @Expose
    private Error error;

    @SerializedName("errors")
    @Expose
    private List<ErrorMsg> errors = null;

    @SerializedName("response")
    @Expose
    private SpeechResponse speechResponse;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public SpeechResponse getSpeechResponse() {
        return speechResponse;
    }

    public void setSpeechResponse(SpeechResponse speechResponse) {
        this.speechResponse = speechResponse;
    }

}