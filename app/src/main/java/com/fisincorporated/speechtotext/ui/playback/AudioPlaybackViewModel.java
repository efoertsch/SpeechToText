package com.fisincorporated.speechtotext.ui.playback;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextConversionData;
import com.fisincorporated.speechtotext.databinding.AudioPlaybackBinding;
import com.fisincorporated.speechtotext.ui.AudioBaseViewModel;
import com.fisincorporated.speechtotext.ui.MediaPlayerAndController;
import com.fisincorporated.speechtotext.ui.signin.SignInActivity;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.netty.util.internal.StringUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.ObjectChangeSet;
import io.realm.RealmObjectChangeListener;


//TODO figure out better way to handle Realm object updates
public class AudioPlaybackViewModel extends AudioBaseViewModel {
    private static final String TAG = AudioPlaybackViewModel.class.getSimpleName();

    private AudioPlaybackBinding viewDataBinding;

    private Context context;

    private AudioRecord audioRecord;

    private AudioRecordUtils audioRecordUtils;

    private MediaPlayerAndController mediaPlayerAndController;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final RealmObjectChangeListener<AudioRecord> audioRecordChangeListener = new RealmObjectChangeListener<AudioRecord>() {
        @Override
        public void onChange(AudioRecord audioRecord, ObjectChangeSet changeSet) {

            if (audioRecord != null) {
                updateDisplay(audioRecord);

            }
            for (String fieldName : changeSet.getChangedFields()) {
                Log.i(TAG, "Field " + fieldName + " was changed.");
            }
        }
    };

    private void updateDisplay(AudioRecord audioRecord) {
        this.audioRecord = audioRecordUtils.copyToNonRealmAudioRecord(audioRecord);
        viewDataBinding.setData(this.audioRecord);
        viewDataBinding.executePendingBindings();
    }

    @Inject
    public AudioPlaybackViewModel(Context context, AudioRecordUtils audioRecordUtils) {
        this.context = context;
        this.audioRecordUtils = audioRecordUtils;
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


    public AudioPlaybackViewModel setView(View view) {
        View bindingView = view.findViewById(R.id.audio_playback_coordinator_layout);
        viewDataBinding = DataBindingUtil.bind(bindingView);
        mediaPlayerAndController = viewDataBinding.audioPlaybackMediaController;
        viewDataBinding.audioPlaybackDescription.setMovementMethod(new ScrollingMovementMethod());
        return this;
    }

    public AudioPlaybackViewModel setAudioRecord(long audioRecordId) {
        if (audioRecordId != 0) {
            AudioRecord realmAudioRecord = audioRecordUtils.getRealmAudioRecord(audioRecordId);
            if (realmAudioRecord != null) {
                realmAudioRecord.addChangeListener(audioRecordChangeListener);
                audioRecord = audioRecordUtils.copyToNonRealmAudioRecord(realmAudioRecord);
                if (audioRecord != null) {
                    String audioFile = audioRecordUtils.getAbsoluteFileName(audioRecord.getAudioFileName());
                    mediaPlayerAndController.setAudioFile(audioFile);
                    viewDataBinding.setData(audioRecord);
                    viewDataBinding.setViewModel(this);
                    setButtonVisibility(true);
                    turnOffEditTextEnabled();
                    viewDataBinding.executePendingBindings();
                }
            }
        }
        return this;
    }

    private void setButtonVisibility(boolean displayFab) {
        viewDataBinding.audioPlaybackFloatingActionButton.setVisibility(displayFab ? View.VISIBLE : View.GONE);
        viewDataBinding.audioPlaybackSaveBtn.setVisibility(displayFab ? View.GONE : View.VISIBLE);
    }

    public void editDescriptionAndText() {
        viewDataBinding.audioPlaybackDescription.setEnabled(true);
        viewDataBinding.audioPlaybackSpeechToText.setEnabled(audioRecord.getXlatJobNumber() == 0);
        if (audioRecord.getXlatJobNumber() <= 0 && StringUtil.isNullOrEmpty(audioRecord.getSpeechToTextTranslation())) {
            displayXlatingText();
        }
        setButtonVisibility(false);
    }

    private void turnOffEditTextEnabled() {
        viewDataBinding.audioPlaybackDescription.setEnabled(false);
        viewDataBinding.audioPlaybackSpeechToText.setEnabled(false);
    }

    private void displayXlatingText() {
        Toast.makeText(context, R.string.translation_in_progress, Toast.LENGTH_LONG).show();
    }


    public void translateToText() {
        if (viewDataBinding.audioPlaybackFloatingActionButton.getVisibility() == View.VISIBLE) {
            removePriorText();
            startTranslationProcess();
        } else {
            Toast.makeText(context, R.string.translation_not_allowed_during_editing, Toast.LENGTH_SHORT).show();
        }
    }

    private void removePriorText() {
        audioRecord.setSpeechToTextTranslation("");
        audioRecordUtils.updateAudioRecordAsync(audioRecord);
        viewDataBinding.executePendingBindings();
    }

    private void startTranslationProcess() {
        SpeechToTextConversionData speechToTextConversionData = new SpeechToTextConversionData(audioRecord.getId(), audioRecord.getDescription(), audioRecordUtils.getAudioDirectoryPath(), audioRecord.getAudioFileName());
        Gson gson = new Gson();
        String jsonData = gson.toJson(speechToTextConversionData);

        Intent intent = SignInActivity.IntentBuilder.getBuilder().setSpeechToTextData(jsonData).build(context);
        context.startActivity(intent);
    }


    // Called via xml onClick
    public void updateAudioRecord() {
        audioRecordUtils.updateAudioRecordAsync(audioRecord);
        setButtonVisibility(true);
        turnOffEditTextEnabled();
    }

    void stopPlaying() {
        mediaPlayerAndController.pause();
    }

}
