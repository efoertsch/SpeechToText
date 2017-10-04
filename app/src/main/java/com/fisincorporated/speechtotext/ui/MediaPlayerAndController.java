package com.fisincorporated.speechtotext.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fisincorporated.speechtotext.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


// cribbed from https://github.com/jaydeepw/audio-wife then modified into view

public class MediaPlayerAndController extends LinearLayout implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener {

    private static final String TAG = MediaPlayerAndController.class.getSimpleName();

    /****
     * Playback progress update time in milliseconds
     ****/
    private static final int AUDIO_PROGRESS_UPDATE_TIME = 100;

    // TODO: externalize the error messages.
    private static final String ERROR_PLAYVIEW_NULL = "Play view cannot be null";
    private static final String ERROR_PLAYTIME_CURRENT_NEGATIVE = "Current playback time cannot be negative";
    private static final String ERROR_PLAYTIME_TOTAL_NEGATIVE = "Total playback time cannot be negative";

    private Handler mProgressUpdateHandler;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private View playButtonView;
    private View pauseButtonView;

    /***
     * Indicates the current run-time of the audio being played
     */
    private TextView runTimeView;

    /***
     * Indicates the total duration of the audio being played.
     */
    private TextView totalTimeView;

    /****
     * Array to hold custom completion listeners
     ****/
    private ArrayList<OnCompletionListener> mCompletionListeners = new ArrayList<OnCompletionListener>();

    private ArrayList<OnClickListener> mPlayListeners = new ArrayList<View.OnClickListener>();

    private ArrayList<View.OnClickListener> mPauseListeners = new ArrayList<View.OnClickListener>();

    public MediaPlayerAndController(Context context) {
        this(context, null);
    }

    public MediaPlayerAndController(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.audio_controller, this, true);
        playButtonView = findViewById(R.id.audio_controller_play);
        pauseButtonView = findViewById(R.id.audio_controller_pause);
        totalTimeView = (TextView) findViewById(R.id.audio_controller_total_time);
        runTimeView = (TextView) findViewById(R.id.audio_controller_run_time);
        seekBar = (SeekBar) findViewById(R.id.audio_controller_media_seekbar);
        mProgressUpdateHandler = new Handler();

    }

    private Runnable updateProgress = new Runnable() {

        public void run() {

            if (seekBar == null) {
                return;
            }

            if (mProgressUpdateHandler != null && mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                int currentTime = mediaPlayer.getCurrentPosition();
                updateRuntime(currentTime);
                // repeat the process
                startProgressBarUpdate(this);
            } else {
                // DO NOT update UI if the player is paused
            }
        }
    };

    /***
     * Starts playing audio file associated. Before playing the audio, visibility of appropriate UI
     * controls is made visible. Calling this method has no effect if the audio is already being
     * played.
     ****/
    public void play(String audioFileName) {

        initPlayer(audioFileName);
        initOnPlayClick();
        initOnPauseClick();
        updateRuntime(0);
        setTotalTime();
        initMediaSeekBar();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(mOnCompletion);

        if (playButtonView == null) {
            throw new IllegalStateException(ERROR_PLAYVIEW_NULL);
        }

        if (mediaPlayer == null) {
            throw new IllegalStateException("Call init() before calling this method");
        }

        if (mediaPlayer.isPlaying()) {
            return;
        }

        startProgressBarUpdate(updateProgress);

        // enable visibility of all UI controls.
        setViewsVisibility();

        mediaPlayer.start();

        setPausable();
    }

    /****
     * Initialize and prepare the audio player
     ****/
    private void initPlayer(String audioFileName) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(audioFileName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(mOnCompletion);
    }

    private void initOnPlayClick() {
        if (playButtonView == null) {
            throw new NullPointerException(ERROR_PLAYVIEW_NULL);
        }

        // add default click listener to the top
        // so that it is the one that gets fired first
        mPlayListeners.add(0, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                setPausable();
                startProgressBarUpdate(updateProgress);
            }
        });

        // Fire all the attached listeners
        // when the play button is clicked
        playButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (View.OnClickListener listener : mPlayListeners) {
                    listener.onClick(v);
                }
            }
        });
    }

    private void initOnPauseClick() {
        if (pauseButtonView == null) {
            throw new NullPointerException("Pause view cannot be null");
        }

        // add default click listener to the top
        // so that it is the one that gets fired first
        mPauseListeners.add(0, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pause();
            }
        });

        // Fire all the attached listeners
        // when the pause button is clicked
        pauseButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (View.OnClickListener listener : mPauseListeners) {
                    listener.onClick(v);
                }
            }
        });
    }


    /**
     * Ensure the views are visible before playing the audio.
     */
    private void setViewsVisibility() {

        if (seekBar != null) {
            seekBar.setVisibility(View.VISIBLE);
        }

        if (runTimeView != null) {
            runTimeView.setVisibility(View.VISIBLE);
        }

        if (totalTimeView != null) {
            totalTimeView.setVisibility(View.VISIBLE);
        }

        if (playButtonView != null) {
            playButtonView.setVisibility(View.VISIBLE);
        }

        if (pauseButtonView != null) {
            pauseButtonView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("DefaultLocale")
    private void updateRuntime(int currentTime) {

        if (runTimeView == null) {
            // this view can be null if the user
            // does not want to use it. Don't throw
            // an exception.
            return;
        }

        if (currentTime < 0) {
            throw new IllegalArgumentException(ERROR_PLAYTIME_CURRENT_NEGATIVE);
        }

        StringBuilder playbackStr = new StringBuilder();

        // set the current time
        // its ok to show 00:00 in the UI
        playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) currentTime), TimeUnit.MILLISECONDS.toSeconds((long) currentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) currentTime))));

        runTimeView.setText(playbackStr);

        // DebugLog.i(currentTime + " / " + totalDuration);
    }

    @SuppressLint("DefaultLocale")
    private void setTotalTime() {

        if (totalTimeView == null) {
            // this view can be null if the user
            // does not want to use it. Don't throw
            // an exception.
            return;
        }

        StringBuilder playbackStr = new StringBuilder();
        long totalDuration = 0;

        // by this point the media player is brought to ready state
        // by the call to init().
        if (mediaPlayer != null) {
            try {
                totalDuration = mediaPlayer.getDuration();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        if (totalDuration < 0) {
            throw new IllegalArgumentException(ERROR_PLAYTIME_TOTAL_NEGATIVE);
        }

        // set total time as the audio is being played
        if (totalDuration != 0) {
            playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(totalDuration), TimeUnit.MILLISECONDS.toSeconds(totalDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration))));
        }

        totalTimeView.setText(playbackStr);
    }

    /***
     * Changes audiowife state to enable play functionality.
     */
    private void setPlayable() {
        if (playButtonView != null) {
            playButtonView.setVisibility(View.VISIBLE);
        }

        if (pauseButtonView != null) {
            pauseButtonView.setVisibility(View.GONE);
        }
    }

    /****
     * Changes audio wife to enable pause functionality.
     */
    private void setPausable() {
        if (playButtonView != null) {
            playButtonView.setVisibility(View.GONE);
        }

        if (pauseButtonView != null) {
            pauseButtonView.setVisibility(View.VISIBLE);
        }
    }


    public void pause() {
        if (mediaPlayer == null) {
            return;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            setPlayable();
        }
    }

    private void startProgressBarUpdate(Runnable updateProgress) {
        mProgressUpdateHandler.postDelayed(updateProgress, AUDIO_PROGRESS_UPDATE_TIME);
    }

    private OnCompletionListener mOnCompletion = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // set UI when audio finished playing
            int currentPlayTime = 0;
            seekBar.setProgress(currentPlayTime);
            //updatePlaytime(currentPlayTime);
            updateRuntime(currentPlayTime);
            setPlayable();
            // ensure that our completion listener fires first.
            // This will provide the developer to over-ride our
            // completion listener functionality

            fireCustomCompletionListeners(mp);
        }
    };

    private void initMediaSeekBar() {

        if (seekBar == null) {
            return;
        }

        // update seekbar
        long finalTime = mediaPlayer.getDuration();
        seekBar.setMax((int) finalTime);

        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

                // if the audio is paused and seekbar is moved,
                // update the play time in the UI.
                updateRuntime(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }

    private void fireCustomCompletionListeners(MediaPlayer mp) {
        for (OnCompletionListener listener : mCompletionListeners) {
            listener.onCompletion(mp);
        }
    }


    /***
     * Releases the allocated resources.
     * */
    public void release() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            mProgressUpdateHandler = null;
        }
    }


    public void onPrepared(MediaPlayer mediaPlayer) {
       // TODO - anything?
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Context context = getContext();
        StringBuilder sb = new StringBuilder();
        if (extra == MediaPlayer.MEDIA_ERROR_TIMED_OUT) {
            sb.append(context.getString(R.string.media_player_error_load_timeout));
        } else if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            sb.append(context.getString(R.string.media_player_error_mserver_unaccessible));
        } else {
            sb.append( context.getString(R.string.media_player_error_unknown_error));
        }
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_IO:
                sb.append(context.getString(R.string.media_player_error_io_error));
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                sb.append( context.getString(R.string.media_player_error_malformed_bitstream));
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                sb.append(context.getString(R.string.media_player_error_unsupported_media));
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                sb.append(context.getString(R.string.media_player_error_timed_out));
        }
        Log.d(TAG, sb.toString());
        mediaPlayer.reset();
        return true;
    }

    /****
     * Add custom playback completion listener. Adding multiple listeners will queue up all the
     * listeners and fire them on media playback completes.
     */
    public MediaPlayerAndController addOnCompletionListener(OnCompletionListener listener) {

        // add default click listener to the top
        // so that it is the one that gets fired first
        mCompletionListeners.add(0, listener);

        return this;
    }

    /****
     * Add custom play view click listener. Calling this method multiple times will queue up all the
     * listeners and fire them all together when the event occurs.
     ***/
    public  MediaPlayerAndController  addOnPlayClickListener(View.OnClickListener listener) {

        mPlayListeners.add(listener);

        return this;
    }

    /***
     * Add custom pause view click listener. Calling this method multiple times will queue up all
     * the listeners and fire them all together when the event occurs.
     ***/
    public  MediaPlayerAndController  addOnPauseClickListener(View.OnClickListener listener) {

        mPauseListeners.add(listener);

        return this;
    }
}

