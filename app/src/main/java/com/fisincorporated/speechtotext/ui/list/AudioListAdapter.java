package com.fisincorporated.speechtotext.ui.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.audio.PlayAudioCallback;
import com.fisincorporated.speechtotext.audio.data.AudioRecord;

import javax.inject.Inject;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

// TODO
// Update code to use Realm with Databinding - https://medium.com/@Zhuinden/realm-1-2-0-android-data-binding-1dc06822287f
public class AudioListAdapter extends RealmRecyclerViewAdapter<AudioRecord, AudioListAdapter.MyViewHolder> {

    private PlayAudioCallback playAudioCallback;

    @Inject
    public AudioListAdapter(Context context, OrderedRealmCollection<AudioRecord> data) {
        super(context, data, true);
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_record_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AudioRecord audioRecord = getItem(position);
        holder.data = audioRecord;
        holder.title.setText((audioRecord.getDescription()));
        holder.recordDate.setText(audioRecord.getRecordDateTime());

        if (playAudioCallback != null) {
            holder.itemView.setOnClickListener(v -> playAudioCallback.playAudioRecord(audioRecord));
            holder.playAudioButton.setOnClickListener(v -> playAudioCallback.playAudioRecord(audioRecord));
        }
        holder.speechToTextView.setVisibility((audioRecord.getSpeechToTextTranslation() != null
                && !audioRecord.getSpeechToTextTranslation().isEmpty()
                && !audioRecord.getSpeechToTextTranslation().equals("\n")) ? View.VISIBLE : View.GONE);

    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    public void setPlayAudioCallback(PlayAudioCallback playAudioCallback) {
        this.playAudioCallback = playAudioCallback;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView recordDate;
        ImageButton playAudioButton;
        TextView speechToTextView;

        public AudioRecord data;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.audio_record_description);
            recordDate = view.findViewById(R.id.audio_record_date);
            playAudioButton = view.findViewById(R.id.audio_record_play_image);
            speechToTextView = view.findViewById(R.id.audio_record_speech_to_text);

        }
    }
}