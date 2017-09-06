package com.fisincorporated.speechtotext.audio.utils;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class SpeechToTextConversionData implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceAbsolutePathToAudioFile);
        dest.writeString(this.audio3gpFileName);
        dest.writeString(this.audioFlacFileName);
        dest.writeByte(this.flacConversionSuccess ? (byte) 1 : (byte) 0);
        dest.writeByte(this.signinToFirebaseSuccess ? (byte) 1 : (byte) 0);
        dest.writeByte(this.uploadToGcsSuccess ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.gcsAudioFileName, flags);
        dest.writeByte(this.audioToTextSuccess ? (byte) 1 : (byte) 0);
        dest.writeString(this.audioText);
        dest.writeSerializable(this.exception);
    }

    protected SpeechToTextConversionData(Parcel in) {
        this.deviceAbsolutePathToAudioFile = in.readString();
        this.audio3gpFileName = in.readString();
        this.audioFlacFileName = in.readString();
        this.flacConversionSuccess = in.readByte() != 0;
        this.signinToFirebaseSuccess = in.readByte() != 0;
        this.uploadToGcsSuccess = in.readByte() != 0;
        this.gcsAudioFileName = in.readParcelable(Uri.class.getClassLoader());
        this.audioToTextSuccess = in.readByte() != 0;
        this.audioText = in.readString();
        this.exception = (Exception) in.readSerializable();
    }

    public static final Creator<SpeechToTextConversionData> CREATOR = new Creator<SpeechToTextConversionData>() {
        @Override
        public SpeechToTextConversionData createFromParcel(Parcel source) {
            return new SpeechToTextConversionData(source);
        }

        @Override
        public SpeechToTextConversionData[] newArray(int size) {
            return new SpeechToTextConversionData[size];
        }
    };
}
