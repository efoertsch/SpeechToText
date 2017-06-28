package com.fisincorporated.speechtotext.ui.record;



import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioUtils;
import com.fisincorporated.speechtotext.databinding.AudioDescriptionInputBinding;
import com.fisincorporated.speechtotext.databinding.AudioRecordBinding;
import com.fisincorporated.speechtotext.ui.AudioBaseViewModel;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;

public class AudioRecordViewModel extends AudioBaseViewModel implements  FinishedRecordingCallback {

    private View bindingView;

    private Context context;

    private Realm realm;

    private View microphoneLayout;

    private AudioRecord audioRecord;

    private AudioRecordBinding viewDataBinding;

    private AudioService audioService;

    private FinishedRecordingCallback finishedRecordingCallBack;


    @Inject
    public AudioRecordViewModel(Context context, AudioService audioService, Realm realm) {
        this.context = context;
        this.audioService = audioService;
        this.realm = realm;
    }

    public AudioRecordViewModel setView(View view) {
        bindingView = view.findViewById(R.id.activity_record_layout);
        viewDataBinding = DataBindingUtil.bind(bindingView);
        viewDataBinding.setViewmodel(this);

        microphoneLayout = viewDataBinding.activityAudioRecordMicrophoneLayout;
        microphoneLayout.setOnClickListener(v -> {
            stopRecording();
            getAudioDescription();
        });

        startRecording();
        return this;
    }

    public AudioRecordViewModel setFinishedRecordingCallback(FinishedRecordingCallback finishedRecordigCallback){
        this.finishedRecordingCallBack = finishedRecordigCallback;
        return this;
    }

    public void startRecording() {
        String audioFileName = UUID.randomUUID().toString() + ".3gp";
        String absoluteFilename = AudioUtils.getAbsoluteFileName(context, audioFileName);
        audioRecord = AudioUtils.createAudioRecord(new Date(), audioFileName);
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
                    if (description != null) {
                        AudioUtils.saveDescriptionWithAudio(audioRecord, description);
                        dialogBox.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel_delete_audio,
                        (dialogBox, id) -> {
                           // deleteAudio();
                            dialogBox.dismiss();
                            finish();
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCanceledOnTouchOutside(false);
        alertDialogAndroid.show();

    }

    private void finish(){
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

