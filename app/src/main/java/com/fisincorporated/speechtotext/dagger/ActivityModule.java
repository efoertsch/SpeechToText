package com.fisincorporated.speechtotext.dagger;


import android.app.Activity;
import android.content.Context;

import com.fisincorporated.speechtotext.audio.AudioRecord;
import com.fisincorporated.speechtotext.audio.AudioRecordAdapter;
import com.fisincorporated.speechtotext.audio.AudioRecorderViewModel;
import com.fisincorporated.speechtotext.dagger.custom.ActivityContext;

import dagger.Module;
import dagger.Provides;
import io.realm.OrderedRealmCollection;

@Module
public class ActivityModule {


    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }


    @Provides
    public AudioRecorderViewModel providesAudioRecorderViewModel() {
        return new AudioRecorderViewModel();

    }

    @Provides
    public AudioRecordAdapter provideAudioRecordAdapter(Context context, OrderedRealmCollection<AudioRecord> data) {
        return new AudioRecordAdapter(context, data);
    }

    @Provides
    public Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    public Context providesContext() {
        return activity;
    }

    @Provides
    public OrderedRealmCollection<AudioRecord> providesOrderedRealmCollection(){
        return null;
    }

}
