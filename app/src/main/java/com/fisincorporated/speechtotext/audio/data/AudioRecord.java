package com.fisincorporated.speechtotext.audio.data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class AudioRecord extends RealmObject implements AudioRecordInfo {

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

    private String speachToTextTranslation;

    @Ignore
    private boolean changed;

    @Ignore
    private ChangeListener changeListener;


    @Inject
    public AudioRecord() {
    }

    public void setChangeListener(ChangeListener listener){
        this.changeListener = listener;
    }

    public AudioRecord(long id, String audioFileName) {
        this.id = id;
        this.audioFileName = audioFileName;
        recordDateTime = recordDate.format(new Date(id));
        noteRecordChanged();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        recordDateTime = recordDate.format(new Date(id));
        noteRecordChanged();

    }

    public String getRecordDateTime() {
        return recordDateTime;
    }

    public void setRecordDateTime(String recordDateTime) {
        this.recordDateTime = recordDateTime;
        noteRecordChanged();
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
        noteRecordChanged();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        noteRecordChanged();
    }

    public String getSpeachToTextTranslation() {
        return speachToTextTranslation;
    }

    public void setSpeachToTextTranslation(String speachToTextTranslation) {
        this.speachToTextTranslation = speachToTextTranslation;
        noteRecordChanged();
    }

    public void noteRecordChanged(){
        this.changed = true;
        if (changeListener != null) {
            changeListener.onChange();
        }
    }

    public boolean isChanged() {
        return changed;
    }

}