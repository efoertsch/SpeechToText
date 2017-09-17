package com.fisincorporated.speechtotext.audio.utils;



import android.content.Context;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.googlespeech.speechrequest.LongRunningRecognize;
import com.fisincorporated.speechtotext.googlespeech.speechresponse.Alternative;
import com.fisincorporated.speechtotext.googlespeech.speechresponse.OperationResponse;
import com.fisincorporated.speechtotext.googlespeech.speechresponse.Result;
import com.fisincorporated.speechtotext.googlespeech.speechresponse.SpeechResponse;
import com.fisincorporated.speechtotext.retrofit.GcsApi;
import com.fisincorporated.speechtotext.retrofit.GcsRetrofit;
import com.fisincorporated.speechtotext.retrofit.GoogleSpeechRetrofit;
import com.fisincorporated.speechtotext.retrofit.GoogleSpeechServicesApi;
import com.fisincorporated.speechtotext.retrofit.LoggingInterceptor;
import com.google.api.client.repackaged.org.apache.commons.codec.EncoderException;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//TODO - Cleanup
//TODO - modify to use injection
public class SpeechToTextService {

    private static final String TAG = SpeechToTextService.class.getSimpleName();

    private Context context;

    private Retrofit gcsRetrofit;

    private Retrofit googleSpeechRetrofit;

    @Inject
    public SpeechToTextService(Context context) {
        //this.storageRef = storageReference;
        // TODO inject
        gcsRetrofit = new GcsRetrofit(new LoggingInterceptor()).getRetrofit();
        googleSpeechRetrofit = new GoogleSpeechRetrofit(new LoggingInterceptor()).getRetrofit();
        this.context = context;
    }

    /**
     * Complete process
     * 1. Convert 3gp to flac audio file
     * 2. Upload flac file to gcs
     * 3. Convert flac file to text
     * TODO
     * 4. Delete flac file on gcs and app device
     *
     * @param speechToTextConversionData
     * @return observable process
     */
    public Observable<SpeechToTextConversionData> getSpeechToTextObservable(SpeechToTextConversionData speechToTextConversionData) {

        Observable<SpeechToTextConversionData> observable = Observable.concatArray(
                audio3GpToFlacObservable(speechToTextConversionData)
                , uploadFileToGcsObservable(speechToTextConversionData)
                , startLongRunningRecognizeObservable(speechToTextConversionData)
//                , checkLongRunningObservable(speechToTextConversionData)
                , startIntermittentCheckOnLongRunningObservable(speechToTextConversionData)
                //, convertGcsAudioFileToTextObservable(speechToTextConversionData)
                , updateRealmObservable(speechToTextConversionData, context));

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
                        // TODO can get this from converted file?
                        speechToTextConversionData.setMimeType("audio/flac");
                        Log.d(TAG, "Conversion status " + speechToTextConversionData.toString());
                        emitter.onNext(speechToTextConversionData);
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(Exception error) {
                        Log.d(TAG, "Error converting:" + speechToTextConversionData.getAbsolute3gpFileName() + " :" + error.toString());
                        speechToTextConversionData.setException(error);
                        speechToTextConversionData.setFlacConversionSuccess(false);
                        emitter.onError(error);
                    }
                };
                AndroidAudioConverter.with(context)
                        // Your current audio file
                        .setFile(inputFile)

                        // Your desired audio format
                        .setFormat(AudioFormat.FLAC)

                        // A callback to know when conversion is finished
                        .setCallback(callback)

                        // Start conversion
                        .convert();
            }
        });
        return observable;
    }

    Observable<SpeechToTextConversionData> uploadFileToGcsObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {
                GcsApi client = gcsRetrofit.create(GcsApi.class);

                // create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse(speechToTextConversionData.getMimeType())
                                , new File(speechToTextConversionData.getAbsoluteFlacFileName()));

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.create(requestFile);

                Call<OperationResponse> call = client.callGcsUpload("Bearer " + speechToTextConversionData.getOauth2Token()
                        , requestFile.contentType().toString(), body, speechToTextConversionData.getAudioFlacFileName());

                call.enqueue(new Callback<OperationResponse>() {
                    @Override
                    public void onResponse(Call<OperationResponse> call,
                                           Response<OperationResponse> response) {
                        if (response.code() == 200) {
                            // TODO set speechToTextConversionData to successful upload
                            speechToTextConversionData.setUploadToGcsSuccess(true);
                            speechToTextConversionData.setGcsAudioFileName(response.body().getName());
                            emitter.onNext(speechToTextConversionData);
                            emitter.onComplete();
                            Log.d(TAG, "success");
                        } else {
                            Log.d(TAG, "unsuccessful");
                            speechToTextConversionData.setUploadToGcsSuccess(false);
                            speechToTextConversionData.setException(new EncoderException(response.toString()));
                            emitter.onError(new Exception(response.toString()));
                        }

                    }

                    @Override
                    public void onFailure(Call<OperationResponse> call, Throwable t) {
                        speechToTextConversionData.setUploadToGcsSuccess(false);
                        emitter.onError(t);
                        Log.e("Upload error:", t.getMessage());
                    }
                });
            }
        });
        return observable;
    }

    //TODO - if this method is kept do better job of resolving RecongnitionConfig and RecognitionAudio conflicts
    Observable<SpeechToTextConversionData> startLongRunningRecognizeObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {
                final Call<OperationResponse> call = getLongRunningRecognizeCall(speechToTextConversionData);
                call.enqueue(new Callback<OperationResponse>() {
                    @Override
                    public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                        OperationResponse operation;
                        if (response.isSuccessful()) {
                            operation = response.body();
                            if (operation.getError() == null && operation.getName() != null) {
                                speechToTextConversionData.setLongRunningRecognizeName(operation.getName());
                                emitter.onNext(speechToTextConversionData);
                                emitter.onComplete();
                            }
                        } else {
                            operation = response.body();
                            if (operation.getError() != null && response.body().getError() != null) {
                                speechToTextConversionData.setLongRunningRecognizeError(response.body().getError().toString());
                            } else {
                                speechToTextConversionData.setLongRunningRecognizeError("Unspecified error");
                            }
                            emitter.onError(new Exception("Unspecified error"));
                        }
                    }

                    @Override
                    public void onFailure(Call<OperationResponse> call, Throwable t) {
                        // something went completely south (like no internet connection)
                        Log.d("Error", t.getMessage());
                        emitter.onError(new Exception(t));
                    }
                });
            }
        });
        return observable;
    }

    Observable<SpeechToTextConversionData> startIntermittentCheckOnLongRunningObservable(SpeechToTextConversionData speechToTextConversionData) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {

                Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                        .takeWhile(val -> (!speechToTextConversionData.isTranslationDone() && speechToTextConversionData.getLongRunningRecognizeError().isEmpty()))
                        .map(val -> {
                            Call<OperationResponse> call = getOperationResponseCall(speechToTextConversionData);
                            OperationResponse operationResponse = call.execute().body();
                            return operationResponse;
                        })
                        .subscribe(operationResponse -> {
                            // onNext
                             if (operationResponse != null && operationResponse.getDone() != null) {
                                speechToTextConversionData.setTranslationDone(operationResponse.getDone());
                                speechToTextConversionData.setAudioToTextSuccess(true);
                                speechToTextConversionData.setAudioText(getTranslatedText(operationResponse.getSpeechResponse()));
                                }
                            }, // onError
                                e -> {
                                    speechToTextConversionData.setLongRunningRecognizeError(e.toString());
                                    speechToTextConversionData.setAudioToTextSuccess(false);
                                    emitter.onError(e);
                                }
                            , // onComplete
                                ()-> emitter.onComplete());
            }
        });

        return observable;
    }
    

    private Call<OperationResponse> getLongRunningRecognizeCall(SpeechToTextConversionData speechToTextConversionData) {
        GoogleSpeechServicesApi client = getGoogleSpeechServicesApi();

        final Call<OperationResponse> call;

        com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionConfig recognitionConfig = new com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionConfig();
        recognitionConfig.setEncoding(com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionConfig.AudioEncoding.FLAC.name());
        // TODO - 16000 may not apply for 3gp to flac converted file
        // SampleRateHertz not needed for FLAC file as info in flac header
        recognitionConfig.setSampleRateHertz(8000);
        // TODO set up option for encoding
        recognitionConfig.setLanguageCode("en-US");
        recognitionConfig.setMaxAlternatives(2);
        recognitionConfig.setProfanityFilter(true);
        // TODO - set up entry screen for list of hint text/phrases
        //recognitionConfig.setSpeechContexts();

        com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionAudio recognitionAudio = new com.fisincorporated.speechtotext.googlespeech.speechrequest.RecognitionAudio();
        //recognitionAudio.setUri("gs://speechtotext-f653f.appspot.com/audio/" + speechToTextConversionData.getGcsAudioFileName());
        recognitionAudio.setUri("gs://speechtotext-f653f.appspot.com/" + speechToTextConversionData.getGcsAudioFileName());

        LongRunningRecognize longRunningRecognize = new LongRunningRecognize();
        longRunningRecognize.setConfig(recognitionConfig);
        longRunningRecognize.setAudio(recognitionAudio);

        call = client.callSpeechToTextApi("Bearer " + speechToTextConversionData.getOauth2Token(), longRunningRecognize);
        return call;
    }

    private Call<OperationResponse> getOperationResponseCall(SpeechToTextConversionData speechToTextConversionData) {
        GoogleSpeechServicesApi client = getGoogleSpeechServicesApi();
        Call<OperationResponse> call;
        call = client.callLongRunningRecognizeProgress("Bearer " + speechToTextConversionData.getOauth2Token(), speechToTextConversionData.getLongRunningRecognizeName());
        return call;
    }

    private GoogleSpeechServicesApi getGoogleSpeechServicesApi() {
        return googleSpeechRetrofit.create(GoogleSpeechServicesApi.class);
    }

    private String getTranslatedText(SpeechResponse response) {
        List<Result> results;
        StringBuffer sb = new StringBuffer();
        int alternativeIx = 0;
        if (response == null || response.getResults() == null) {
            return "Invalid response. No List<Results> returned";
        }
        if ((results = response.getResults()).size() == 0) {
            return "No translatable text returned";
        }
        for (Result result : results) {
            if (result.getAlternatives() != null) {
                alternativeIx = 0;
                for (Alternative alternative : result.getAlternatives()) {
                    Log.d(TAG, String.format("Transcription: %s%n", alternative.getTranscript()));
                    sb.append(alternativeIx == 0 ? alternative.getTranscript() : "  [[" + alternative.getTranscript() + "]]" + '\n');
                    alternativeIx++;
                }
            }
        }
        return sb.toString();
    }


    /**
     * Update realm object.
     *
     * @param speechToTextConversionData
     * @return
     */
    // Needs to run on same thread as 'online' realm
    Observable<SpeechToTextConversionData> updateRealmObservable(SpeechToTextConversionData speechToTextConversionData, Context context) {
        Observable<SpeechToTextConversionData> observable = Observable.create(new ObservableOnSubscribe<SpeechToTextConversionData>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<SpeechToTextConversionData> emitter) throws Exception {
                if (!speechToTextConversionData.getAudioText().isEmpty()) {
                    AudioRecordUtils audioRecordUtils = new AudioRecordUtils(context);
                    AudioRecord audioRecord = audioRecordUtils.getAudioRecord(speechToTextConversionData.getAudioRecordRealmId());
                    audioRecord.setSpeechToTextTranslation(speechToTextConversionData.getAudioText());
                    audioRecordUtils.updateAudioRecordSync(audioRecord);
                }
                emitter.onNext(speechToTextConversionData);
                emitter.onComplete();
            }
        });
        return observable;
    }

}