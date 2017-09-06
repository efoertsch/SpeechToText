package com.fisincorporated.speechtotext.translation;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;



public class XlatSchedulerService extends JobService {

    private static final String TAG = XlatSchedulerService.class.getSimpleName();

    /**
     * Called by system to start the job. Note this call is on main thread. So any long running tasks
     * must use another thread to do work
     * @param params
     * @return  true - if job not finished by the time the method returns (e.g. asynctask is started)
     *          false - if job completed by the time the method returns (so task MUST be quick about it)
     */
    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "onStartJob called");

        return false;
    }

    /**
     * Called by system to cancel pending tasks when a cancel request is receieved.
     * Only called if onStartJob returns false
     * @param params
     * @return true if to reschedule job based on previous scheduling criteria
     *         false if to drop the job
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob called");

        return false;
    }

    /**
     * Call when job completes
     * @param params Original JobParameters
     * @param needsRescheduled - true to reschedule (say for recurring task)
     *                           false to not reschedule (all done)
     */
    public void jobCompleted(JobParameters params, boolean needsRescheduled) {
        //jobFinished(JobParameters params, boolean needsRescheduled)
        jobFinished(params, false);
    }

}
