package com.fisincorporated.speechtotext.googlespeech.objectaccess;

import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ObjectAccessList extends BaseJson {

    @SerializedName("items")
    @Expose
    private ObjectAccess[] objectAccessList;

    private String kind;

    public ObjectAccess[] getObjectAccessList ()
    {
        return objectAccessList;
    }

    public void setObjectAccess (ObjectAccess[] objectAccessList)
    {
        this.objectAccessList = objectAccessList;
    }

    public String getKind ()
    {
        return kind;
    }

    public void setKind (String kind)
    {
        this.kind = kind;
    }
}
