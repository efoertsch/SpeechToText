package com.fisincorporated.speechtotext.googlespeech.objectaccess;

import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProjectTeam extends BaseJson {

    @SerializedName("team")
    @Expose
    private String team;

    @SerializedName("projectNumber")
    @Expose
    private String projectNumber;

    public String getTeam ()
    {
        return team;
    }

    public void setTeam (String team)
    {
        this.team = team;
    }

    public String getProjectNumber ()
    {
        return projectNumber;
    }

    public void setProjectNumber (String projectNumber)
    {
        this.projectNumber = projectNumber;
    }
}
