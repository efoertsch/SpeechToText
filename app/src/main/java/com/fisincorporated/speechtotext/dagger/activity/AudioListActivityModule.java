package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.list.AudioListActivity;
import com.fisincorporated.speechtotext.ui.list.AudioListAdapter;
import com.fisincorporated.speechtotext.ui.list.AudioListViewModel;

import dagger.Module;
import dagger.Provides;
import io.realm.OrderedRealmCollection;

@Module
@PerActivity
public class AudioListActivityModule {

    @Provides
    public AudioListViewModel providesAudioListViewModel(AudioListActivity activity, AudioRecordUtils audioRecordUtils) {
        return new AudioListViewModel(activity,  providesAudioListAdapter(activity, audioRecordUtils), audioRecordUtils);
    }

    @Provides
    public AudioListAdapter providesAudioListAdapter(AudioListActivity activity,   AudioRecordUtils audioRecordUtils){
        return new AudioListAdapter(activity, providesOrderedRealmCollection(audioRecordUtils));
    }

    @Provides
    public OrderedRealmCollection<AudioRecord> providesOrderedRealmCollection(AudioRecordUtils audioRecordUtils){
        return audioRecordUtils.getOrderedRealmCollection();
    }
}
