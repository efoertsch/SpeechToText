package com.fisincorporated.speechtotext.audio.utils;

import android.content.Context;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.data.AudioRecord;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.content.ContentValues.TAG;

public class AudioUtils {

    private static Context context;

    // Application context
    public static void setContext(Context context) {
        AudioUtils.context = context;
    }

    public static void configureRealm() {
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


    public static String getAbsoluteFileName(String filename) {
        return context.getFilesDir() + "/" + filename;
    }

    public static AudioRecord createAudioRecord(final Realm realm, final Date currentDate, final String audioFileName) {
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
        return (list.size() > 0 ? list.get(0) : null);
    }

    public static void saveDescriptionWithAudio(final Realm realm, final AudioRecord audioRecord, final String description) {
        realm.executeTransaction(realm1 -> {
            audioRecord.setDescription(description);
            realm1.insertOrUpdate(audioRecord);
        });
    }

    public static List<AudioRecord> getAllAudioRecords(Realm realm) {
        List<AudioRecord> list = realm.where(AudioRecord.class).findAll();
        return list;
    }

    public static File[] getAudioFiles() {
        File f = context.getFilesDir();
        FilenameFilter audioFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".3gp")) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        return f.listFiles(audioFilter);
    }

    public static void deleteAudioFile(String filename) {
        File file = new File(getAbsoluteFileName(filename));
        file.delete();
    }

    public static void listAudioFiles() {
        File[] files = AudioUtils.getAudioFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; ++i) {
                Log.d(TAG, files[i].getName());
            }
        }
    }


    // To fix "oops"
    public static void createMissingAudioRecords() {
        boolean recordExists;
        Realm realm = Realm.getDefaultInstance();
        File[] files = getAudioFiles();
        List<AudioRecord> list = getAllAudioRecords(realm);
        if (files != null && files.length > 0) {
            for (File file : files) {
                recordExists = false;
                for (AudioRecord audioRecord : list) {
                    if (file.getName().equals(audioRecord.getAudioFileName())) {
                        recordExists = true;
                        break;
                    }
                }
                if (!recordExists) {
                    AudioUtils.createAudioRecord(realm, new Date(), file.getName());
                    Log.d(TAG, "created audio record for " + file.getName());
                }
            }
        }

        realm.close();


    }


}
