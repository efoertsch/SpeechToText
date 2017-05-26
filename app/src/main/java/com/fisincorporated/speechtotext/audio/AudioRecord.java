package com.fisincorporated.speechtotext.audio;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

public class AudioRecord extends RealmObject {

    private static final SimpleDateFormat recordDate = new SimpleDateFormat("yyyy-MM-dd:HH:mm", Locale.getDefault());

    @Index
    private long millisecId;

    @Required
    private String recordDateTime;

    @Required
    private String audioFileName;

    private String description;

    public AudioRecord(){}

    public AudioRecord(long millisecId, String audioFileName ){
        this.millisecId = millisecId;
        this.audioFileName = audioFileName;
        recordDateTime = recordDate.format(new Date(millisecId));
    }

    public long getMillisecId() {
        return millisecId;
    }

    public void setMillisecId(long millisecId) {
        this.millisecId = millisecId;
    }

    private String translatedText;


    public String getRecordDateTime() {
        return recordDateTime;
    }

    public void setRecordDateTime(String recordDateTime) {
        this.recordDateTime = recordDateTime;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }



}