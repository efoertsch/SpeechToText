package com.fisincorporated.speechtotext.utils;


public class StringUtils {

    public static boolean  isEmpty(String string){
        return (string == null || string.isEmpty());
    }

    public static boolean isNotEmpty(String string){
        return (string != null && !string.isEmpty());
    }

}
