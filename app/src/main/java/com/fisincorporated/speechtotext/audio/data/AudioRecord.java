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

    public enum AUDIO_TO_TEXT_STATUS {
        ERROR(-1),
        NOT_STARTED(0),
        RUNNING(1),
        FINISHED(2);
        private final int value;

        private AUDIO_TO_TEXT_STATUS(final int value) {
            this.value = value;
        }
        public static AUDIO_TO_TEXT_STATUS fromValue(int value)
                throws IllegalArgumentException {
            try {
                return AUDIO_TO_TEXT_STATUS.values()[value];
            } catch(ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Unknown enum value :"+ value);
            }
        }
    }

    @PrimaryKey
    @Index
    private long id;

    @Required
    private String recordDateTime;

    @Required
    private String audioFileName;

    private String description;

    //TODO change name
    private String speechToTextTranslation;

    private int speechToTextStatus = 0;

    private int xlatJobNumber = 0;

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

    public String getSpeechToTextTranslation() {
        return speechToTextTranslation;
    }

    public void setSpeechToTextTranslation(String speechToTextTranslation) {
        this.speechToTextTranslation = speechToTextTranslation;
        noteRecordChanged();
    }

    public void noteRecordChanged(){
        this.changed = true;
        if (changeListener != null) {
            changeListener.onChange();
        }
    }

    public void setChanged(boolean changed){
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
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