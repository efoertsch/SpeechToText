package com.fisincorporated.speechtotext.audio.data;

public interface AudioRecordInfo {

    String getRecordDateTime();

    String getAudioFileName();

    String getDescription();

    void setDescription(String description);

    String getSpeechToTextTranslation();

    void setSpeechToTextTranslation(String speechToTextTranslation);

    int getSpeechToTextStatus();

    void setSpeechToTextStatus(int speechToTextStatus);

}
