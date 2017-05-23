package com.fisincorporated.speechtotext;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class AudioRecordRecyclerView extends RealmRecyclerViewAdapter<AudioRecord, AudioRecordRecyclerView.MyViewHolder> {

    private boolean inDeletionMode = false;
    private Set<String> AudioRecordsToDelete = new HashSet<>();
    private Context context;

   public AudioRecordRecyclerView(Context context, OrderedRealmCollection<AudioRecord> data) {
        super(context, data, true);
        setHasStableIds(true);
    }

    public void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
        if (!enabled) {
            AudioRecordsToDelete.clear();
        }
        notifyDataSetChanged();
    }

    public Set<String> getAudioRecordsToDelete() {
        return AudioRecordsToDelete;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AudioRecord obj = getItem(position);
        holder.data = obj;
        holder.title.setText(obj.getRecordDateTime());
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getMillisecId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton playAudioButton;
        public AudioRecord data;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.audio_record_description);
            playAudioButton = (ImageButton) view.findViewById(R.id.audio_record_play_image);
        }
    }
}