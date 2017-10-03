package com.fisincorporated.speechtotext.jobscheduler;



// From http://www.vogella.com/tutorials/AndroidTaskScheduling/article.html and modified as needed.

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.utils.SpeechToTextConversionData;
import com.google.gson.Gson;

import javax.inject.Inject;

public class JobSchedulerUtil {

    private static final String TAG = JobSchedulerUtil.class.getSimpleName();

    @Inject
    public JobSchedulerUtil(){}

    // schedule the job.
    public void scheduleXlatJob(Context context, SpeechToTextConversionData speechToTextConversionData) {
        int rc = 0;
        ComponentName serviceComponent = new ComponentName(context, TranslationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        // Can't store data as parcelable for job params, need to convert to Json and store that
        PersistableBundle bundle = new PersistableBundle();
        Gson g = new Gson();
        String json = g.toJson(speechToTextConversionData);
        bundle.putString(SpeechToTextConversionData.SPEECH_TO_TEXT_CONVERSION_DATA, json);
        builder.setExtras(bundle);

        //builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // Will use cellular if no wi-fi. Do we want to do that?
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        //builder.setPersisted(true);  // persist job over reboot
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        rc = jobScheduler.schedule(builder.build());
        if (rc <= 0 ){
            Log.d(TAG, "Oops! An error occurred when trying to schedule a xlat job. rc:" + rc);
        };
        //TOD place rc (really the job number in the audio object for later reference
    }
}

