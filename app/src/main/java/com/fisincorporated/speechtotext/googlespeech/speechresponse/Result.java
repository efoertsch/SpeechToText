package com.fisincorporated.speechtotext.googlespeech.speechresponse;

import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result extends BaseJson {

    @SerializedName("alternatives")
    @Expose
    private List<Alternative> alternatives = null;

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

}
