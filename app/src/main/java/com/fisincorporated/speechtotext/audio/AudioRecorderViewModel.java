package com.fisincorporated.speechtotext.audio;


import android.content.ContextWrapper;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.application.ViewModelLifeCycle;
import com.fisincorporated.speechtotext.databinding.AudioRecordingBinding;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class AudioRecorderViewModel implements ViewModelLifeCycle {

    private File audioDirectory;

    private View bindingView;

    private AudioRecordingBinding viewDataBinding;

    private AudioRecord audioRecord;

    private View microphoneLayout;

    private FloatingActionButton microphoneFab;

    private Realm realm;

    public AudioService audioService;

    public AudioRecordAdapter audioRecordAdapter;

    @Inject
    public AudioRecorderViewModel(AudioService audioService, AudioRecordAdapter audioRecordAdapter) {
        this.audioService = audioService;
        this.audioRecordAdapter = audioRecordAdapter;
        realm = Realm.getDefaultInstance();
    }

    public AudioRecorderViewModel setView(View view) {

        bindingView = view.findViewById(R.id.activity_recorder_layout);
        viewDataBinding = DataBindingUtil.bind(bindingView);
        setupRecyclerView(viewDataBinding.activityRecorderRecyclerView);
        viewDataBinding.setViewmodel(this);

        // Record to the external cache directory for visibility
        audioDirectory = (new ContextWrapper(view.getContext())).getFilesDir();

        microphoneFab = viewDataBinding.activityRecorderMicrophoneFab;
        microphoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                microphoneLayout.setVisibility(View.VISIBLE);
                microphoneFab.setVisibility(View.GONE);
                onRecord(true);
            }
        });

        microphoneLayout = viewDataBinding.activityAudioRecordMicrophoneLayout;
        microphoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphoneLayout.setVisibility(View.GONE);
                microphoneFab.setVisibility(View.VISIBLE);
                onRecord(false);
            }
        });

        return this;
    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(audioRecordAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST));
        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void onRecord(boolean start) {
        if (start) {
            String audioFileName = UUID.randomUUID().toString() + ".3gp";
            audioRecord = createAudioRecord(new Date(), audioFileName);
            audioService.startRecording(audioFileName);
        } else {
            audioService.stopRecording();
        }
    }

    private AudioRecord createAudioRecord(final Date currentDate, final String audioFileName) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AudioRecord audioRecord = realm.createObject(AudioRecord.class);
                audioRecord.setMillisecId(currentDate.getTime());
                audioRecord.setAudioFileName(audioDirectory + audioFileName);
                realm.insertOrUpdate(audioRecord);

            }
        });

        RealmResults<AudioRecord> list = realm.where(AudioRecord.class).equalTo("millisecId", currentDate.getTime()).findAll();
        return (list.size() > 0 ? list.get(0) : null);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        audioService.stopPlaying();
        audioService.stopRecording();
    }

    @Override
    public void onDestroy() {
        realm.close();
    }

    private class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        TouchHelperCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            deleteItemAsync(realm, viewHolder.getItemId());
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    }

    public static void deleteItemAsync(Realm realm, final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AudioRecord.delete(realm, id);
            }
        });
    }

    public static void deleteItemsAsync(Realm realm, Collection<Integer> ids) {
        // Create an new array to avoid concurrency problem.
        final Long[] idsToDelete = new Long[ids.size()];
        ids.toArray(idsToDelete);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Long id : idsToDelete) {
                    AudioRecord.delete(realm, id);
                }
            }
        });
    }

}
