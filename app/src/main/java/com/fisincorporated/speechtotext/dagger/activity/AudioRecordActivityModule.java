package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.AudioRecord;
import com.fisincorporated.speechtotext.audio.AudioRecordActivity;
import com.fisincorporated.speechtotext.audio.AudioRecordAdapter;
import com.fisincorporated.speechtotext.audio.AudioRecorderViewModel;
import com.fisincorporated.speechtotext.audio.AudioService;

import dagger.Module;
import dagger.Provides;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;

@Module
public class AudioRecordActivityModule {


    @Provides
    public AudioRecorderViewModel providesAudioRecorderViewModel(AudioRecordActivity activity) {
        return new AudioRecorderViewModel(new AudioService(), providesAudioRecordAdapter(activity));
    }

    @Provides
    public AudioRecordAdapter providesAudioRecordAdapter(AudioRecordActivity activity){
        return new AudioRecordAdapter(activity, providesOrderedRealmCollection());
    }

    @Provides
    public OrderedRealmCollection<AudioRecord> providesOrderedRealmCollection(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(AudioRecord.class).findAllSorted(AudioRecord.FIELDS.audioFileName.name());
    }
}
