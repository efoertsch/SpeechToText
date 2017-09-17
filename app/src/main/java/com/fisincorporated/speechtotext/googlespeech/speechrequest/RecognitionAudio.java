package com.fisincorporated.speechtotext.googlespeech.speechrequest;



import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecognitionAudio extends BaseJson {

    //Either content or uri must be supplied. Supplying both or neither returns google.rpc.Code.INVALID_ARGUMENT

    //The audio data bytes encoded as specified in RecognitionConfig.
    // Note: as with all bytes fields, protobuffers use a pure binary representation,
    // whereas JSON representations use base64.
    @SerializedName("content")
    @Expose
    private String content;

    //URI that points to a file that contains audio data bytes as specified in RecognitionConfig.
    // Currently, only Google Cloud Storage URIs are supported,
    // which must be specified in the following format:
    // gs://bucket_name/object_name (other URI formats return google.rpc.Code.INVALID_ARGUMENT)
    @SerializedName("uri")
    @Expose
    private String uri;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
