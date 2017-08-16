package com.fisincorporated.speechtotext.audio.utils;



import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

//TODO - Cleanup
public class SpeechToTextService {

    private static final String TAG = SpeechToTextService.class.getSimpleName();

    private StorageReference storageRef;

    private Activity activity;

    @Inject
    public SpeechToTextService(Activity activity, StorageReference storageReference) {
        this.storageRef = storageReference;
        this.activity = activity;
    }

    public Observable<SpeechToTextConversionData> getSpeechToTextObservable(SpeechToTextConversionData speechToTextConversionData) {

        Observable<SpeechToTextConversionData> observable = Observable.concat(audio3GpToFlacObservable(speechToTextConversionData), signonToFirebaseObservable(speechToTextConversionData)
                , uploadFlacFileToFirebaseObservable(speechToTextConversionData), convertGcsAudioFileToTextObservable(speechToTextConversionData));

        return observable;

    }

    /**
     * Convert audio file in 3gp format to flac
     *
     * @param speechToTextConversionData
     * @return
     */
    public Observable<SpeechToTextConversionData> audio3GpToFlacObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {
                Log.d(TAG, " audio3GpToFlacObservable(. uploadFlacFileToFirebaseObservable. Current thread:" + Thread.currentThread());
                File inputFile = new File(speechToTextConversionData.getAbsolute3gpFileName());
                IConvertCallback callback = new IConvertCallback() {
                    @Override
                    public void onSuccess(File convertedFile) {
                        Log.d(TAG, "Converted:" + speechToTextConversionData.getAudio3gpFileName() + " to " + convertedFile);
                        speechToTextConversionData.setAudioFlacFileName(convertedFile.getName());
                        Log.d(TAG, " Conversion status " + speechToTextConversionData.toString());
                        //emitter.onNext(speechToTextConversionData);
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.d(TAG, "Error converting:" + speechToTextConversionData.getAbsolute3gpFileName() + " :" + error.toString());
                        speechToTextConversionData.setException(error);
                        emitter.onError(error);
                    }
                };
                AndroidAudioConverter.with(activity)
                        // Your current audio file
                        .setFile(inputFile)

                        // Your desired audio format
                        .setFormat(AudioFormat.FLAC)

                        // An callback to know when conversion is finished
                        .setCallback(callback)

                        // Start conversion
                        .convert();
            }
        });
        return observable;
    }


    public void convert3gpToFlac(final String absolute3GpFileName) {
        File inputFile = new File(absolute3GpFileName);
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                Log.d(TAG, "Converted:" + absolute3GpFileName + " to " + convertedFile);

            }

            @Override
            public void onFailure(Exception error) {
                Log.d(TAG, "Error converting:" + absolute3GpFileName + " :" + error.toString());
            }
        };
        AndroidAudioConverter.with(activity)
                // Your current audio file
                .setFile(inputFile)
                // Your desired audio format
                .setFormat(AudioFormat.FLAC)
                // An callback to know when conversion is finished
                .setCallback(callback)
                // Start conversion
                .convert();
    }

    public Observable<SpeechToTextConversionData> signonToFirebaseObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {
                Log.d(TAG, " signonToFirebaseObservable. uploadFlacFileToFirebaseObservable. Current thread:" + Thread.currentThread());
                // TODO - Remove anonymous sign in
                // Sign in anonymously. Authentication is required to read or write from Firebase Storage.
                // firebaseAuth.signInWithEmailAndPassword("emailaddress@something.com", "pwd")
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInAnonymously()
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.d(TAG, "signInAnonymously:SUCCESS");
                                speechToTextConversionData.setSigninToFirebaseSuccess(true);
                                //emitter.onNext(speechToTextConversionData);
                                emitter.onComplete();
                            }
                        })
                        .addOnFailureListener(activity, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                speechToTextConversionData.setSigninToFirebaseSuccess(false);
                                Log.e(TAG, "signInAnonymously:FAILURE", exception);
                                emitter.onError(exception);
                            }
                        });
            }
        });
        return observable;
    }

    public Observable<SpeechToTextConversionData> uploadFlacFileToFirebaseObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {
                Log.d(TAG, " uploadFlacFileToFirebaseObservable. Current thread:" + Thread.currentThread());
                Uri file = Uri.fromFile(new File(speechToTextConversionData.getAbsoluteFlacFileName()));
                StorageReference audioFileStorageRef = storageRef.child("audio/" + speechToTextConversionData.getAudioFlacFileName());
                audioFileStorageRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                @SuppressWarnings("VisibleForTests")
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                try {
                                    speechToTextConversionData.setUploadToGcsSuccess(true);
                                    speechToTextConversionData.setGcsAudioFileName(downloadUrl);
                                    // emitter.onNext(speechToTextConversionData);
                                    emitter.onComplete();
                                } catch (Exception e) {
                                    speechToTextConversionData.setUploadToGcsSuccess(false);
                                    emitter.onError(e);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                speechToTextConversionData.setUploadToGcsSuccess(false);
                                emitter.onError(exception);

                            }
                        });
            }
        });
        return observable;
    }

    public Observable<SpeechToTextConversionData> convertGcsAudioFileToTextObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {

                try {
                    // TODO - Add API calls to start/download speech to text file
                    // TODO - Delete file on Firebase when done

                    emitter.onComplete();

                } catch (Exception e) {
                    speechToTextConversionData.setAudioToTextSuccess(false);
                    emitter.onError(e);
                    e.printStackTrace();
                }
            }
        });
        return observable;
    }

}
