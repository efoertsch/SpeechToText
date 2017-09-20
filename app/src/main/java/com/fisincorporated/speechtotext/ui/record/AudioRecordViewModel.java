package com.fisincorporated.speechtotext.ui.record;



import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextConversionData;
import com.fisincorporated.speechtotext.databinding.AudioDescriptionInputBinding;
import com.fisincorporated.speechtotext.databinding.AudioRecordBinding;
import com.fisincorporated.speechtotext.ui.AudioBaseViewModel;
import com.fisincorporated.speechtotext.ui.signin.SignInActivity;
import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

public class AudioRecordViewModel extends AudioBaseViewModel implements FinishedRecordingCallback {

    private Context context;

    private AudioRecord audioRecord;

    private AudioService audioService;

    private FinishedRecordingCallback finishedRecordingCallBack;

    private AudioRecordUtils audioRecordUtils;


    @Inject
    public AudioRecordViewModel(Context context, AudioService audioService, AudioRecordUtils audioRecordUtils) {
        this.context = context;
        this.audioService = audioService;
        this.audioRecordUtils = audioRecordUtils;
    }

    public AudioRecordViewModel setView(View view) {
        View bindingView = view.findViewById(R.id.activity_record_layout);
        AudioRecordBinding viewDataBinding = DataBindingUtil.bind(bindingView);
        viewDataBinding.setViewmodel(this);

        View microphoneLayout = viewDataBinding.activityAudioRecordMicrophoneLayout;
        microphoneLayout.setOnClickListener(v -> {
            stopRecording();
            getAudioDescription();
        });

        startRecording();
        return this;
    }

    public AudioRecordViewModel setFinishedRecordingCallback(FinishedRecordingCallback finishedRecordigCallback) {
        this.finishedRecordingCallBack = finishedRecordigCallback;
        return this;
    }

    private void startRecording() {
        String audioFileName = UUID.randomUUID().toString() + ".3gp";
        String absoluteFilename = audioRecordUtils.getAbsoluteFileName(audioFileName);
        audioRecord = audioRecordUtils.createAudioRecord(new Date(), audioFileName);
        audioService.startRecording(absoluteFilename);
    }

    public void stopRecording() {
        audioService.stopRecording();
    }


    private void getAudioDescription() {
        AudioDescriptionInputBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.audio_description_input, null, false);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(binding.getRoot());
        binding.setViewModel(this);

        final EditText userInputDialogEditText = binding.userInputDialog;
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.save, (dialogBox, id) -> {
                    String description = userInputDialogEditText.getText().toString();
                    audioRecordUtils.saveDescriptionWithAudio(audioRecord, description);
                    dialogBox.dismiss();
                    signInForTranslation(audioRecord);
                    finish();
                })
                .setNegativeButton(R.string.cancel_delete_audio,
                        (dialogBox, id) -> {
                            deleteAudio();
                            dialogBox.dismiss();
                            finish();
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCanceledOnTouchOutside(false);
        alertDialogAndroid.show();

    }

    private void signInForTranslation(AudioRecord audioRecord) {

        SpeechToTextConversionData speechToTextConversionData = new SpeechToTextConversionData(audioRecord.getId(), audioRecordUtils.getAudioDirectoryPath(), audioRecord.getAudioFileName());
        Gson gson = new Gson();
        String jsonData = gson.toJson(speechToTextConversionData);

        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(SpeechToTextConversionData.SPEECH_TO_TEXT_CONVERSION_DATA, jsonData);
        context.startActivity(intent);
    }

    private void deleteAudio() {
        audioRecordUtils.deleteAudioRecord(audioRecord);
    }

    private void finish() {
        audioService.stopRecording();
        finishedRecording();
    }

    @Override
    public void finishedRecording() {
        if (finishedRecordingCallBack != null) {
            finishedRecordingCallBack.finishedRecording();
        }
    }
}

