package com.fisincorporated.speechtotext.audio.data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class AudioRecord extends RealmObject implements AudioRecordInfo {

    public static final String AUDIO_RECORD = "AudioRecord";

    private static final SimpleDateFormat recordDate = new SimpleDateFormat("yyyy-MM-dd:HH:mm", Locale.getDefault());

    public interface ChangeListener {
        void onChange();
    }

    public enum FIELDS {
        id,
        recordDataTime,
        audioFileName,
        description;
    }

    @PrimaryKey
    @Index
    private long id;

    @Required
    private String recordDateTime;

    @Required
    private String audioFileName;

    private String description;

    private String speechToTextTranslation;

    private int speechToTextStatus = 0;

    /**
     * -1 translation not started
     * 0 - translation completed (successfully or not)
     * >0 - jobId of translation job
     */
    private int xlatJobNumber = -1;

    @Inject
    public AudioRecord() {
    }

    public AudioRecord(long id, String audioFileName) {
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

    public String getSpeechToTextTranslation() {
        return speechToTextTranslation;
    }

    public void setSpeechToTextTranslation(String speechToTextTranslation) {
        this.speechToTextTranslation = speechToTextTranslation;
    }

    public int getSpeechToTextStatus() {
        return speechToTextStatus;
    }

    public void setSpeechToTextStatus(int speechToTextStatus) {
        this.speechToTextStatus = speechToTextStatus;
    }

    public int getXlatJobNumber() {
        return xlatJobNumber;
    }

    public void setXlatJobNumber(int xlatJobNumber) {
        this.xlatJobNumber = xlatJobNumber;
    }
}