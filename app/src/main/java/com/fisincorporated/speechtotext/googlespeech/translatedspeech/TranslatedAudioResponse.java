package com.fisincorporated.speechtotext.googlespeech.translatedspeech;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranslatedAudioResponse {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("done")
    @Expose
    private Boolean done;
    @SerializedName("response")
    @Expose
    private Response response;

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

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}