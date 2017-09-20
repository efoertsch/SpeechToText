package com.fisincorporated.speechtotext.googlespeech.speechresponse;


public class TranslatedResults {
    private Result[] results;

    private String totalBilledTime;

    public Result[] getResults ()
    {
        return results;
    }

    public void setResults (Result[] results)
    {
        this.results = results;
    }

    public String getTotalBilledTime ()
    {
        return totalBilledTime;
    }

    public void setTotalBilledTime (String totalBilledTime)
    {
        this.totalBilledTime = totalBilledTime;
    }
}
