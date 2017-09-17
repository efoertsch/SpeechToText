package com.fisincorporated.speechtotext.ui.list;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.DividerItemDecoration;
import com.fisincorporated.speechtotext.audio.PlayAudioCallback;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.databinding.AudioListBinding;
import com.fisincorporated.speechtotext.ui.AudioBaseViewModel;
import com.fisincorporated.speechtotext.ui.playback.AudioPlaybackActivity;
import com.fisincorporated.speechtotext.ui.record.AudioRecordActivity;

import javax.inject.Inject;

public class AudioListViewModel extends AudioBaseViewModel implements PlayAudioCallback  {

    private View bindingView;

    private AudioListBinding viewDataBinding;

    private AudioRecord audioRecord;

    private FloatingActionButton microphoneFab;

    private RecyclerView recyclerView;

    private Context context;

    private AudioListAdapter audioListAdapter;

    private AudioRecordUtils audioRecordUtils;

    @Inject
    public AudioListViewModel(Context context, AudioListAdapter audioListAdapter, AudioRecordUtils audioRecordUtils) {
        this.context = context;
        this.audioListAdapter = audioListAdapter;
        this.audioRecordUtils = audioRecordUtils;
        audioListAdapter.setPlayAudioCallback(this);
    }

    public AudioListViewModel setView(View view) {
        bindingView = view.findViewById(R.id.activity_list_layout);
        viewDataBinding = DataBindingUtil.bind(bindingView);
        recyclerView = viewDataBinding.activityListAudioRecyclerView;
        setupRecyclerView(recyclerView);
        viewDataBinding.setViewmodel(this);

        microphoneFab = viewDataBinding.activityListMicrophoneFab;
        microphoneFab.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, AudioRecordActivity.class);
            context.startActivity(intent);
        });

        return this;
    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(audioListAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST));
        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void playAudioRecord(AudioRecord audioRecord) {
        // TODO mode to play activity
        Intent intent = new Intent(context, AudioPlaybackActivity.class);
        intent.putExtra(AudioPlaybackActivity.AUDIO_ID, audioRecord.getId());
        context.startActivity(intent);
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
            audioRecordUtils.deleteAudioRecordByIdAsync(viewHolder.getItemId());
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

    }

}
