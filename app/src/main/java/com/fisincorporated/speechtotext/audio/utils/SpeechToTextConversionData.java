package com.fisincorporated.speechtotext.audio.utils;


import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


//TODO remove unneeded fields/cleanup
public class SpeechToTextConversionData extends BaseJson {

    public static final String SPEECH_TO_TEXT_CONVERSION_DATA = "SPEECH_TO_TEXT_CONVERSION_DATA";

    @SerializedName("audioDescripton")
    @Expose
    private String audioDescripton;

    @SerializedName("deviceAbsolutePathToAudioFile")
    @Expose
    private String deviceAbsolutePathToAudioFile = "";

    @SerializedName("audio3gpFileName")
    @Expose
    private String audio3gpFileName = "";

    @SerializedName("audioFlacFileName")
    @Expose
    private String audioFlacFileName = "";

    @SerializedName("flacConversionSuccess")
    @Expose
    private boolean flacConversionSuccess = false;

    @SerializedName("uploadToGcsSuccess")
    @Expose
    private boolean uploadToGcsSuccess = false;

    @SerializedName("gcsAudioFileName")
    @Expose
    private String gcsAudioFileName ;

    @SerializedName("audioToTextSuccess")
    @Expose
    private boolean audioToTextSuccess = false;

    @SerializedName("mimeType")
    @Expose
    private String mimeType = "";

    @SerializedName("audioText")
    @Expose
    private String audioText = "";

    @SerializedName("exception")
    @Expose
    private Exception exception;

    @SerializedName("oauth2Token")
    @Expose
    private String oauth2Token = "";

    @SerializedName("audioRecordRealmId")
    @Expose
    private long audioRecordRealmId = 0;

    @SerializedName("longunningrecognizeid")
    @Expose
    private String longRunningRecognizeName = "";

    @SerializedName("longrunningrecognizeerror")
    @Expose
    private String longRunningRecognizeError = "";

    @SerializedName("updateToRealmSuccess")
    @Expose
    private boolean updateToRealmSuccess;

    @SerializedName("translationAttempts")
    @Expose
    private int translationAttempts = 0;

    @SerializedName("translationDone")
    @Expose
    private boolean translationDone = false;

    @SerializedName("driveFileFolder")
    @Expose
    private String driveFileFolder = "";

    @SerializedName("driveFileName")
    @Expose
    private String driveFileName = "";



    private SpeechToTextConversionData(){}

    public SpeechToTextConversionData(long audioRecordRealmId, String description
            , String deviceAbsolutePathToAudioFile, String audio3gpFileName, String driveFileFolder){
        this.audioRecordRealmId = audioRecordRealmId;
        this.audioDescripton = description;
        this.deviceAbsolutePathToAudioFile = deviceAbsolutePathToAudioFile;
        this.audio3gpFileName = audio3gpFileName;
        this.driveFileFolder = driveFileFolder;
    };

    public String getAudioDescripton() {
        return audioDescripton;
    }

    public void setAudioDescripton(String audioDescripton) {
        this.audioDescripton = audioDescripton;
    }

    public String getDeviceAbsolutePathToAudioFile() {
        return deviceAbsolutePathToAudioFile;
    }

    public String getAudio3gpFileName() {
        return audio3gpFileName;
    }

    public String getAbsolute3gpFileName() {
        return deviceAbsolutePathToAudioFile +  audio3gpFileName;
    }

    public String getAudioFlacFileName() {
        return audioFlacFileName;
    }

    public String getAbsoluteFlacFileName() {
         return deviceAbsolutePathToAudioFile + audioFlacFileName;
    }

    public void setAudioFlacFileName(String audioFlacFileName) {
        this.audioFlacFileName = audioFlacFileName;
    }

    public boolean isFlacConversionSuccess() {
        return flacConversionSuccess;
    }

    public void setFlacConversionSuccess(boolean flacConversionSuccess) {
        this.flacConversionSuccess = flacConversionSuccess;
    }

    public boolean isUploadToGcsSuccess() {
        return uploadToGcsSuccess;
    }

    public void setUploadToGcsSuccess(boolean uploadToGcsSuccess) {
        this.uploadToGcsSuccess = uploadToGcsSuccess;
    }

    public String getGcsAudioFileName() {
        return gcsAudioFileName;
    }

    public void setGcsAudioFileName(String gcsAudioFileName) {
        this.gcsAudioFileName = gcsAudioFileName;
    }

    public boolean isAudioToTextSuccess() {
        return audioToTextSuccess;
    }

    public void setAudioToTextSuccess(boolean audioToTextSuccess) {
        this.audioToTextSuccess = audioToTextSuccess;
    }

    public String getAudioText() {
        return audioText;
    }

    public void setAudioText(String audioText) {
        this.audioText = audioText;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getOauth2Token() {
        return oauth2Token;
    }

    public void setOauth2Token(String oauth2Token) {
        this.oauth2Token = oauth2Token;
    }

    public long getAudioRecordRealmId() {
        return audioRecordRealmId;
    }


    public void setAudioRecordRealmId(long audioRecordRealmId) {
        this.audioRecordRealmId = audioRecordRealmId;
    }

    public String getLongRunningRecognizeName() {
        return longRunningRecognizeName;
    }

    public void setLongRunningRecognizeName(String longRunningRecognizeName) {
        this.longRunningRecognizeName = longRunningRecognizeName;
    }

    public String getLongRunningRecognizeError() {
        return longRunningRecognizeError;
    }

    public void setLongRunningRecognizeError(String longRunningRecognizeError) {
        this.longRunningRecognizeError = longRunningRecognizeError;
    }


    public int getTranslationAttempts() {
        return translationAttempts;
    }

    public void setTranslationAttempts(int translationAttempts) {
        this.translationAttempts = translationAttempts;
    }

    public void incrementTranslationAttempts() {
        translationAttempts++;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isTranslationDone() {
        return translationDone;
    }

    public void setTranslationDone(boolean translationDone) {
        this.translationDone = translationDone;
    }

    public String getDriveFileFolder() {
        return driveFileFolder;
    }

    public void setDriveFileFolder(String driveFileFolder) {
        this.driveFileFolder = driveFileFolder;
    }

    public String getDriveFileName() {
        return driveFileName;
    }

    public void setDriveFileName(String driveFileName) {
        this.driveFileName = driveFileName;
    }
}
