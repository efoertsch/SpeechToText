package com.fisincorporated.speechtotext.utils;



import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TokenStorage {


    private static final String TOKEN_DATA_PREFS = "TOKEN_DATA_PREFS";
    private static final String TOKEN_DATA = "TOKEN_DATA";

    public static String getToken(Context context) {
        SharedPreferences tokenDataPrefs = context.getSharedPreferences(TOKEN_DATA, Context.MODE_PRIVATE);
        String tokenDataJson = tokenDataPrefs.getString(TOKEN_DATA, null);
        if (tokenDataJson == null) {
            return null;
        }
        Gson gson = new Gson();
        try {
            TokenData tokenData = gson.fromJson(tokenDataJson, TokenData.class);
            return (TokenData.getTokenIfValid(tokenData));
        } catch (JsonSyntaxException jse) {
            return null;
        }
    }

    public static void storeToken(Context context, String token ) {
        SharedPreferences tokenDataPrefs = context.getSharedPreferences(TOKEN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenDataPrefs.edit();
        if (token == null) {
            editor.putString(TOKEN_DATA, null);
        } else {
                Gson gson = new Gson();
                TokenData tokenData = new TokenData(token);
                editor.putString(TOKEN_DATA, gson.toJson(tokenData));
        }
        editor.apply();
    }


}
