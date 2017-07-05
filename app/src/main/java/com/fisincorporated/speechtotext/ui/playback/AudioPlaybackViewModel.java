package com.fisincorporated.speechtotext.ui.playback;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioUtils;
import com.fisincorporated.speechtotext.databinding.AudioPlaybackBinding;
import com.fisincorporated.speechtotext.ui.AudioBaseViewModel;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;

public class AudioPlaybackViewModel extends AudioBaseViewModel implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,  MediaController.MediaPlayerControl,
        MediaPlayer.OnErrorListener {
    private static final String TAG = AudioPlaybackViewModel.class.getSimpleName();

    private View bindingView;

    private AudioPlaybackBinding viewDataBinding;

    private Context context;

    private Realm realm;

    private AudioService audioService;

    private AudioRecord audioRecord;

    private MediaPlayer mediaPlayer;

    private MediaController mediaController;

    private String audioFile;

    private Handler handler = new Handler();

    @Inject
    public AudioPlaybackViewModel(Context context, AudioService audioService, Realm realm) {
        this.context = context;
        this.audioService = audioService;
        this.realm = realm;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        mediaController = new MediaController(context);
    }

    public void playAudio(long audioRecordId){
        if (audioRecordId != 0) {
            audioRecord = getAudioRecord(audioRecordId);
            if (audioRecord != null) {
                viewDataBinding.setData(audioRecord);
                viewDataBinding.executePendingBindings();
                startPlaying(audioRecord);
            }
        }
    }

    public AudioPlaybackViewModel setView(View view){
        bindingView = view.findViewById(R.id.audio_playback_layout);
        viewDataBinding = DataBindingUtil.bind(bindingView);
        return this;

    }


    private AudioRecord getAudioRecord(long id) {
            Realm realm = Realm.getDefaultInstance();
            return realm.where(AudioRecord.class).equalTo(AudioRecord.FIELDS.id.name(),id).findFirst();
    }

    private  void startPlaying(AudioRecord audioRecord) {
        try {
            audioFile = AudioUtils.getAbsoluteFileName(audioRecord.getAudioFileName());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "Could not open file " + audioFile + " for playback.", e);
        }
    }


    protected void stopPlaying() {
        if (mediaController.isShowing()) {
            mediaController.hide();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        // keep mediaController on screen
        mediaController.show(0);
        return false;
    }

    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaController.show(0);
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(viewDataBinding.audioPlaybackMediaController);

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show(0);
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
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

    @Override
    public void onCompletion(MediaPlayer mp) {
    }


}
