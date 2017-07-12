package com.fisincorporated.speechtotext.googlespeech.translatedspeech;

import com.fisincorporated.speechtotext.googlespeech.translatedspeech.Alternative;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

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
