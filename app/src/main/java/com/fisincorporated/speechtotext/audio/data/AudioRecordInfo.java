package com.fisincorporated.speechtotext.audio.data;

public interface AudioRecordInfo {

    String getRecordDateTime();

    String getAudioFileName();

    String getDescription();

    void setDescription(String description);

    String getSpeachToTextTranslation();

    void setSpeachToTextTranslation(String speachToTextTranslation);

}
