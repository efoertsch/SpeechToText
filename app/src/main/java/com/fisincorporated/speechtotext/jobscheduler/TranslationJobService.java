package com.fisincorporated.speechtotext.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.utils.SpeechToTextConversionData;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;



public class TranslationJobService extends JobService {

    private static final String TAG = TranslationJobService.class.getSimpleName();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public SpeechToTextService speechToTextService;

    /**
     * Called by system to start the job. Note this call is on main thread. So any long running tasks
     * must use another thread to do work
     *
     * @param params Must include PersistableBundle with json string of SpeechToTextData
     * @return true - if job not finished by the time the method returns (e.g. asynctask is started)
     * false - if job completed by the time the method returns (so task MUST be quick about it)
     */
    @Override
    public boolean onStartJob(JobParameters params) {

       // AndroidInjection.inject(this);
        speechToTextService = new SpeechToTextService(this);
        Log.d(TAG, "onStartJob called");
        return startSpeechToTextTranslation(params);

    }

    private SpeechToTextConversionData getConversionData(JobParameters params) {
        PersistableBundle bundle = params.getExtras();
        String conversionJson = bundle.getString(SpeechToTextConversionData.SPEECH_TO_TEXT_CONVERSION_DATA, "");
        Gson g = new Gson();
        try {
            return g.fromJson(conversionJson, SpeechToTextConversionData.class);
        } catch (JsonSyntaxException e) {
            Log.d(TAG, "Error creating SpeechToTextConversionData parm from:" + conversionJson);
        }
        return null;
    }

    /**
     * Called by system to cancel pending tasks when a cancel request is receieved.
     * Only called if onStartJob returns false
     *
     * @param params
     * @return true if to reschedule job based on previous scheduling criteria
     * false if to drop the job
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob called");

        return false;
    }

    /**
     * Call when job completes
     *
     * @param params           Original JobParameters
     * @param needsRescheduled - true to reschedule (say for recurring task)
     *                         false to not reschedule (all done)
     */
    public void jobCompleted(JobParameters params, boolean needsRescheduled) {
        jobFinished(params, needsRescheduled);
    }

    public boolean startSpeechToTextTranslation(JobParameters params) {

        SpeechToTextConversionData speechToTextConversionData = getConversionData(params);
        if (speechToTextConversionData != null) {

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            Observable<SpeechToTextConversionData> observable = speechToTextService.getSpeechToTextObservable(speechToTextConversionData);

            DisposableObserver<SpeechToTextConversionData> observer = new DisposableObserver<SpeechToTextConversionData>() {

                @Override
                public void onError(Throwable e) {
                    //TODO Put out notification of error
                    speechToTextConversionData.incrementTranslationAttempts();
                    if (speechToTextConversionData.getTranslationAttempts() < 2) {
                        //TODO Put out notification of error and restart
                        jobFinished(params, true);
                    } else {
                        //TODO Put out notification of error and stopping translation
                        jobFinished(params, false);
                    }
                    this.dispose();
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onComplete() {
                    //TODO Put out notification of error
                    jobCompleted(params, false);
                    Log.e(TAG, "Translation completed");
                    this.dispose();
                }

                @Override
                public void onNext(SpeechToTextConversionData speechToTextConversionData) {
                    System.out.println("onNext:" + speechToTextConversionData.toString());
                }
            };

            disposables.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                    subscribeWith(observer));

            return false;
        }

        Log.d(TAG, "Job started but missing SpeechToTextData!");
        return true;

    }

}
