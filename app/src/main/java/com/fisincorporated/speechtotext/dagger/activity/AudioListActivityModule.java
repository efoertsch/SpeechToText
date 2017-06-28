package com.fisincorporated.speechtotext.dagger.activity;


import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.dagger.annotations.PerActivity;
import com.fisincorporated.speechtotext.ui.list.AudioListActivity;
import com.fisincorporated.speechtotext.ui.list.AudioListAdapter;
import com.fisincorporated.speechtotext.ui.list.AudioListViewModel;

import dagger.Module;
import dagger.Provides;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;

@Module
@PerActivity
public class AudioListActivityModule {

    @Provides
    public AudioListViewModel providesAudioListViewModel(AudioListActivity activity, @PerActivity Realm realm) {
        return new AudioListViewModel(activity,  providesAudioListAdapter(activity, realm), realm);
    }

    @Provides
    public AudioListAdapter providesAudioListAdapter(AudioListActivity activity,   Realm realm){
        return new AudioListAdapter(activity, providesOrderedRealmCollection(realm));
    }

    @Provides
    public OrderedRealmCollection<AudioRecord> providesOrderedRealmCollection(Realm realm){
        return realm.where(AudioRecord.class).notEqualTo(AudioRecord.FIELDS.id.name(),0).findAllSorted(AudioRecord.FIELDS.audioFileName.name());
    }
}
