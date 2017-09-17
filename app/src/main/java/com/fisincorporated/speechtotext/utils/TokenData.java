package com.fisincorporated.speechtotext.utils;


import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Hold oauth2 token with (approx) time in millisecs that it was generated.
 */
public class TokenData {

    public static final long ONE_HOUR_IN_MILLISECS = 1000 * 60 * 60 ;

    @Expose
    private String token;

    @Expose
    private long createTime;

    public TokenData(String token) {
        this.token = token;
        createTime = new Date().getTime();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public static String getTokenIfValid(TokenData tokenData){
        return (tokenData == null ? null : isTokenStillOK(tokenData) ? tokenData.getToken() : null);
    }

    public static boolean isTokenStillOK(TokenData tokenData) {
        return (tokenData == null ? false : isTokenStillOK(tokenData.getCreateTime()));
    }

    public static boolean isTokenStillOK(long tokenTimeInMillis) {
        return (tokenTimeInMillis + ONE_HOUR_IN_MILLISECS >= (new Date().getTime()));
    }
}
