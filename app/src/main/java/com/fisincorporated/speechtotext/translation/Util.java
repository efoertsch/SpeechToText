package com.fisincorporated.speechtotext.translation;



// From http://www.vogella.com/tutorials/AndroidTaskScheduling/article.html and modified as needed.

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class Util {

    private static final String TAG = Util.class.getSimpleName();

    // schedule the job.
    public static void scheduleXlatJob(Context context) {
        int rc = 0;
        ComponentName serviceComponent = new ComponentName(context, XlatSchedulerService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        //builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // Will use cellular if no wi-fi. Do we want to do that?
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        builder.setPersisted(true);  // persist job over reboot
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        rc = jobScheduler.schedule(builder.build());
        if (rc <= 0 ){
            Log.d(TAG, "Oops! An error occurred when trying to schedule a xlat job. rc:" + rc);
        };
        //TOD place rc (really the job number in the audio object for later reference
    }
}

