package com.fisincorporated.speechtotext.ui.playback;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.view.View;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextConversionData;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextService;
import com.fisincorporated.speechtotext.databinding.AudioPlaybackBinding;
import com.fisincorporated.speechtotext.ui.AudioBaseViewModel;
import com.fisincorporated.speechtotext.ui.MediaPlayerAndController;
import com.fisincorporated.speechtotext.ui.signin.SignInActivity;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class AudioPlaybackViewModel extends AudioBaseViewModel implements AudioRecord.ChangeListener {
    private static final String TAG = AudioPlaybackViewModel.class.getSimpleName();

    private View bindingView;

    private AudioPlaybackBinding viewDataBinding;

    private Context context;

    private AudioService audioService;

    private AudioRecord audioRecord;

    private String audioFile;

    private AudioRecordUtils audioRecordUtils;

    private SpeechToTextService speechToTextService;

    private MediaPlayerAndController mediaPlayerAndController;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public AudioPlaybackViewModel(Context context, AudioService audioService, AudioRecordUtils audioRecordUtils, SpeechToTextService speechToTextService) {
        this.context = context;
        this.audioService = audioService;
        this.audioRecordUtils = audioRecordUtils;
        this.speechToTextService = speechToTextService;
    }

    public AudioPlaybackViewModel setView(View view) {
        bindingView = view.findViewById(R.id.audio_playback_layout);
        viewDataBinding = DataBindingUtil.bind(bindingView);
        mediaPlayerAndController = viewDataBinding.audioPlaybackMediaController;
        mediaPlayerAndController.addOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.pause();
                mp.seekTo(0);
            }
        });
        return this;
    }

    public void playAudio(long audioRecordId) {
        if (audioRecordId != 0) {
            audioRecord = audioRecordUtils.getAudioRecord(audioRecordId);
            audioFile = audioRecordUtils.getAbsoluteFileName(audioRecord.getAudioFileName());
            viewDataBinding.setData(audioRecord);
            viewDataBinding.setViewModel(this);
            viewDataBinding.executePendingBindings();
            // TODO send filename to view to play
            mediaPlayerAndController.play(audioFile);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayerAndController.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerAndController.release();
        disposables.dispose();
    }

    public void translateToText() {
        SpeechToTextConversionData speechToTextConversionData = new SpeechToTextConversionData(audioRecord.getId(), audioRecord.getDescription(), audioRecordUtils.getAudioDirectoryPath(), audioRecord.getAudioFileName());
        Gson gson = new Gson();
        String jsonData = gson.toJson(speechToTextConversionData);

        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(SpeechToTextConversionData.SPEECH_TO_TEXT_CONVERSION_DATA, jsonData);
        context.startActivity(intent);

    }


    @Override
    public void onChange() {

        viewDataBinding.audioPlaybackSaveBtn.setVisibility(View.VISIBLE);
    }

    // Called via xml onClick
    public void updateAudioRecord() {
        audioRecordUtils.updateAudioRecordAsync(audioRecord);
    }

    public void stopPlaying() {
        mediaPlayerAndController.pause();
    }
}
