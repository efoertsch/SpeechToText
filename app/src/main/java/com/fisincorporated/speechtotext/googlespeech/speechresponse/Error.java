package com.fisincorporated.speechtotext.googlespeech.speechresponse;

import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error extends BaseJson {

    @SerializedName("errors")
    @Expose
    private List<ErrorMsg> errors = null;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public List<ErrorMsg> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorMsg> errors) {
        this.errors = errors;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

}