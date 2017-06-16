package com.fisincorporated.speechtotext.audio.record;



import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.application.ViewModelLifeCycle;
import com.fisincorporated.speechtotext.audio.AudioRecord;
import com.fisincorporated.speechtotext.audio.AudioService;
import com.fisincorporated.speechtotext.audio.utils.AudioUtils;
import com.fisincorporated.speechtotext.databinding.AudioDescriptionInputBinding;
import com.fisincorporated.speechtotext.databinding.AudioRecordBinding;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class AudioRecordViewModel implements ViewModelLifeCycle, FinishedRecordingCallback {

    private View bindingView;

    private Context context;

    private Realm realm;

    private View microphoneLayout;

    private AudioRecord audioRecord;

    private AudioRecordBinding viewDataBinding;

    private AudioService audioService;

    private FinishedRecordingCallback finishedRecordingCallBack;


    @Inject
    public AudioRecordViewModel(Context context, AudioService audioService) {
        this.context = context;
        this.audioService = audioService;
        realm = Realm.getDefaultInstance();

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
        audioRecord = createAudioRecord(new Date(), audioFileName);
        audioService.startRecording(absoluteFilename);
    }

    public void stopRecording() {
            audioService.stopRecording();
    }

    private AudioRecord createAudioRecord(final Date currentDate, final String audioFileName) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AudioRecord audioRecord = realm.createObject(AudioRecord.class);
                audioRecord.setId(currentDate.getTime());
                audioRecord.setAudioFileName(audioFileName);
                realm.insertOrUpdate(audioRecord);

            }
        });

        RealmResults<AudioRecord> list = realm.where(AudioRecord.class).equalTo("id", currentDate.getTime()).findAll();
        return (list.size() > 0 ? list.get(0) : null);
    }

    private void saveDescriptionWithAudio(final String description) {
        Realm.getDefaultInstance().executeTransaction(realm1 -> {
            audioRecord.setDescription(description);
            realm1.insertOrUpdate(audioRecord);
        });
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
                        saveDescriptionWithAudio(description);
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
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        audioService.stopRecording();

    }

    @Override
    public void finishedRecording() {
        if (finishedRecordingCallBack != null) {
            finishedRecordingCallBack.finishedRecording();
        }
    }
}

