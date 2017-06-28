package com.fisincorporated.speechtotext.audio.utils;

import android.content.Context;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.data.AudioRecord;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.content.ContentValues.TAG;

public class AudioUtils {

    public static void configureRealm(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("audio.files")
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(AudioRecord.class);
                    }
                })
                .build();
        //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(AudioRecord.class).count();
        Log.d(TAG, "number of audio records:" + count);
        realm.close();
    }


    public static String getAbsoluteFileName(Context context, String filename) {
        return context.getFilesDir() + "/" + filename;
    }

    public static AudioRecord createAudioRecord(final Date currentDate, final String audioFileName) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AudioRecord audioRecord = realm.createObject(AudioRecord.class);
                audioRecord.setId(currentDate.getTime());
                audioRecord.setAudioFileName(audioFileName);
                realm.insertOrUpdate(audioRecord);
            }
        });

        List<AudioRecord> list = realm.where(AudioRecord.class).equalTo("id", currentDate.getTime()).findAll();
        realm.close();
        return (list.size() > 0 ? list.get(0) : null);
    }

    public static void saveDescriptionWithAudio(final AudioRecord audioRecord, final String description) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            audioRecord.setDescription(description);
            realm1.insertOrUpdate(audioRecord);
        });
        realm.close();
    }

    public static List<AudioRecord> getAllAudioRecords() {
        Realm realm = Realm.getDefaultInstance();
        List<AudioRecord> list = realm.where(AudioRecord.class).findAll();
        realm.close();
        return list;

    }

    public static File[] getAudioFiles(Context context) {
        File f = context.getFilesDir();
        return f.listFiles();
    }

}
