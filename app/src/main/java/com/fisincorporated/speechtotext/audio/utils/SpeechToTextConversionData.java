package com.fisincorporated.speechtotext.audio.utils;


import android.net.Uri;

public class SpeechToTextConversionData {

    private String deviceAbsolutePathToAudioFile = "";

    private String audio3gpFileName = "";

    private String audioFlacFileName = "";

    private boolean flacConversionSuccess = false;

    private boolean signinToFirebaseSuccess = false;

    private boolean uploadToGcsSuccess = false;

    private Uri gcsAudioFileName ;

    private boolean audioToTextSuccess = false;

    private String audioText = "";

    private Exception exception;

    private SpeechToTextConversionData(){}

    public SpeechToTextConversionData(String deviceAbsolutePathToAudioFile, String audio3gpFileName){
        this.deviceAbsolutePathToAudioFile = deviceAbsolutePathToAudioFile;
        this.audio3gpFileName = audio3gpFileName;
    };

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

    public boolean isSigninToFirebaseSuccess() {
        return signinToFirebaseSuccess;
    }

    public void setSigninToFirebaseSuccess(boolean signinToFirebaseSuccess) {
        this.signinToFirebaseSuccess = signinToFirebaseSuccess;
    }

    public boolean isUploadToGcsSuccess() {
        return uploadToGcsSuccess;
    }

    public void setUploadToGcsSuccess(boolean uploadToGcsSuccess) {
        this.uploadToGcsSuccess = uploadToGcsSuccess;
    }

    public Uri getGcsAudioFileName() {
        return gcsAudioFileName;
    }

    public void setGcsAudioFileName(Uri gcsAudioFileName) {
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

    public String toString(){
        return " deviceAbsolutePathToAudioFile:" + deviceAbsolutePathToAudioFile
                + "\n3gpFileName: " + audio3gpFileName
                + "\n audioFlacFileName: " + audioFlacFileName
                + "\n converted 3gp to flac: " +  flacConversionSuccess
                + "\n signed in to Firebase successfully: " +  signinToFirebaseSuccess
                + "\n uploaded to Google Cloud Services successfully: " + uploadToGcsSuccess
                + "\n converted audio to text successfully: " + audioToTextSuccess
                + "\n converted text: " + audioText
                + "\n " + (exception == null ? "" : exception.toString());
    }

}
