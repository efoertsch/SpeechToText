package com.fisincorporated.speechtotext.jobscheduler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextConversionData;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextService;
import com.fisincorporated.speechtotext.dagger.service.DaggerTranslationJobServiceComponent;
import com.fisincorporated.speechtotext.dagger.service.TranslationJobServiceModule;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.security.GeneralSecurityException;

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

    @Inject
    public AudioRecordUtils audioRecordUtils;

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

        doInjection();
        //speechToTextService = new SpeechToTextService(this);
        Log.d(TAG, "onStartJob called");
        return startSpeechToTextTranslation(params);
    }

    private void doInjection() {
        DaggerTranslationJobServiceComponent.builder()
                .translationJobServiceModule(new TranslationJobServiceModule(this))
                .build()
                .inject(this);
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
        if (true) {
            try {
                speechToTextService.createDriveService();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        SpeechToTextConversionData speechToTextConversionData = getConversionData(params);

        updateAudioRecordWithJobId(speechToTextConversionData, params);

        if (speechToTextConversionData != null) {
            postNotification(speechToTextConversionData);

            Observable<SpeechToTextConversionData> observable = speechToTextService.getSpeechToTextObservable(speechToTextConversionData);

            DisposableObserver<SpeechToTextConversionData> observer = new DisposableObserver<SpeechToTextConversionData>() {

                @Override
                public void onError(Throwable e) {
                    //TODO implement reattempt better than that below
                    jobFinished(params, false);
                    this.dispose();
                    postNotification(speechToTextConversionData);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onComplete() {
                    postNotification(speechToTextConversionData);
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

    private void updateAudioRecordWithJobId(SpeechToTextConversionData speechToTextConversionData, JobParameters params) {
        AudioRecord audioRecord = audioRecordUtils.getNonRealmAudioRecord(speechToTextConversionData.getAudioRecordRealmId());
        if (audioRecord != null) {
            audioRecord.setXlatJobNumber(params.getJobId());
            audioRecordUtils.updateAudioRecordAsync(audioRecord);
        }
    }

    public void postNotification(SpeechToTextConversionData speechToTextConversionData) {
        String contentText;
        if (StringUtils.isNotEmpty(speechToTextConversionData.getAudioText())) {
            contentText = getString(R.string.speech_to_text_converstion_complete, speechToTextConversionData.getAudioDescripton());
        } else if (speechToTextConversionData.getException() != null) {
            contentText = getString(R.string.oops_an_error_has_occurred, speechToTextConversionData.getAudioDescripton(), speechToTextConversionData.getException().toString());

        } else {
            contentText = getString(R.string.text_to_speech_conversion_proceeding, speechToTextConversionData.getAudioDescripton());
        }
        postNotification(speechToTextConversionData, contentText);
    }

    public void postNotification(SpeechToTextConversionData speechToTextConversionData, String notificationContent) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification_info)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(notificationContent);
        // Creates an explicit intent for an Activity in your app
        Intent intent = new Intent(this, AudioPlaybackActivity.class);
        intent.putExtra(AudioPlaybackActivity.AUDIO_ID, speechToTextConversionData.getAudioRecordRealmId());

        // The stack builder object will contain an artificial back stack for the started Activity.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AudioPlaybackActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mNotificationId is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify((int) speechToTextConversionData.getAudioRecordRealmId(), mBuilder.build());
    }

}
