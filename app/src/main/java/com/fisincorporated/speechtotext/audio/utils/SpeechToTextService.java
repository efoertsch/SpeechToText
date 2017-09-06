package com.fisincorporated.speechtotext.audio.utils;



import android.app.Activity;
import android.net.Uri;
import android.system.Os;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.gax.rpc.OperationFuture;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.longrunning.Operation;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


//TODO - Cleanup
public class SpeechToTextService {

    private static final String TAG = SpeechToTextService.class.getSimpleName();

    private StorageReference storageRef;

    private FirebaseAuth firebaseAuth;

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
                        Log.d(TAG, "Conversion status " + speechToTextConversionData.toString());
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




    public Observable<SpeechToTextConversionData> signonToFirebaseObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {
                Log.d(TAG, " signonToFirebaseObservable. uploadFlacFileToFirebaseObservable. Current thread:" + Thread.currentThread());
                // TODO - Remove anonymous sign in
                // Sign in anonymously. Authentication is required to read or write from Firebase Storage.
                //firebaseAuth.signInWithEmailAndPassword("emailaddress@something.com", "pwd")
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInAnonymously()
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.d(TAG, "signInAnonymously:SUCCESS");
                                speechToTextConversionData.setSigninToFirebaseSuccess(true);
                                emitter.onNext(speechToTextConversionData);
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
                // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
                Os.setenv("GOOGLE_APPLICATION_CREDENTIALS", "google-services.json", true);
                SpeechClient speech = null;
                Log.d(TAG, " convertGcsAudioFileToTextObservable. Current thread:" + Thread.currentThread());
                try {

                    speech = SpeechClient.create();
                    // Configure remote file request for Linear16
                    RecognitionConfig config = RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(16000)
                            .build();
                    RecognitionAudio audio = RecognitionAudio.newBuilder()
                            .setUri(speechToTextConversionData.getGcsAudioFileName().toString())
                            .build();

                    // Use non-blocking call for getting file transcription
                    OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata, Operation> response =
                            speech.longRunningRecognizeAsync(config, audio);
                    while (!response.isDone()) {
                        Log.d(TAG, "Waiting for response...");
                        Thread.sleep(10000);
                    }

                    List<SpeechRecognitionResult> results = response.get().getResultsList();

                    StringBuilder sb = new StringBuilder();
                    int alternativeIx = 0;
                    for (SpeechRecognitionResult result : results) {
                        List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();

                        for (SpeechRecognitionAlternative alternative : alternatives) {
                            Log.d(TAG, String.format("Transcription: %s%n", alternative.getTranscript()));
                            sb.append(alternativeIx == 0 ? alternative.getTranscript() : "(" + alternative.getTranscript() + ")");
                            alternativeIx++;
                        }
                        alternativeIx = 0;

                    }
                    speechToTextConversionData.setAudioText(sb.toString());
                    speech.close();
                    speechToTextConversionData.setAudioToTextSuccess(true);
                    emitter.onNext(speechToTextConversionData);
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
