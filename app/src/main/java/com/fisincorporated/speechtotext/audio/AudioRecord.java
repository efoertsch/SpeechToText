package com.fisincorporated.speechtotext.audio;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

public class AudioRecord extends RealmObject {

    private static final SimpleDateFormat recordDate = new SimpleDateFormat("yyyy-MM-dd:HH:mm", Locale.getDefault());

    public enum FIELDS{
        id,
        recordDataTime,
        audioFileName,
        description;
    }

    @Index
    private long id;

    @Required
    private String recordDateTime;

    @Required
    private String audioFileName;

    private String description;

    private String speachToTextTranslation;


    @Inject
    public AudioRecord(){}

    public AudioRecord(long id, String audioFileName ){
        this.id = id;
        this.audioFileName = audioFileName;
        recordDateTime = recordDate.format(new Date(id));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        recordDateTime = recordDate.format(new Date(id));
    }

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

    public String getSpeachToTextTranslation() {
        return speachToTextTranslation;
    }

    public void setSpeachToTextTranslation(String speachToTextTranslation) {
        this.speachToTextTranslation = speachToTextTranslation;
    }


    public static void delete(Realm realm, long id) {
        AudioRecord audioRecord = realm.where(AudioRecord.class).equalTo(FIELDS.id.name(), id).findFirst();
        // Otherwise it has been deleted already.
        if (audioRecord != null) {
            audioRecord.deleteFromRealm();
        }
    }

}