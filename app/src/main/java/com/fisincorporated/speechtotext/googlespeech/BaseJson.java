package com.fisincorporated.speechtotext.googlespeech;


import com.google.gson.Gson;

public class BaseJson {


    @Override
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
