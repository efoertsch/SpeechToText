package com.fisincorporated.speechtotext.audio.utils;

import android.content.Context;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.data.AudioRecord;
import com.fisincorporated.speechtotext.audio.data.AudioRecordMigration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AudioRecordUtils {

    private static final String TAG = AudioRecordUtils.class.getSimpleName();

    private static final String fileSeparator = "/";

    private Context context;

    private Realm realm;



    @Inject
    public AudioRecordUtils(Context context) {
        this.context = context;
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("audio.files")
                .schemaVersion(1)
                .migration(new AudioRecordMigration())
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(AudioRecord.class);
                    }
                })
                .build();
        //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
        long count = realm.where(AudioRecord.class).count();
        Log.d(TAG, "number of audio records:" + count);
    }

    public OrderedRealmCollection<AudioRecord> getOrderedRealmCollection() {
        return realm.where(AudioRecord.class).notEqualTo(AudioRecord.FIELDS.id.name(), 0).findAllSorted(AudioRecord.FIELDS.audioFileName.name());
    }

    public String getAudioDirectoryPath(){
        return context.getFilesDir() + fileSeparator;
    }

    public String getAbsoluteFileName(String filename) {
        return context.getFilesDir() + fileSeparator + filename;
    }

    public AudioRecord createAudioRecord(final Date currentDate, final String audioFileName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AudioRecord audioRecord = realm.createObject(AudioRecord.class, currentDate.getTime());
                audioRecord.setAudioFileName(audioFileName);
                realm.insertOrUpdate(audioRecord);
            }
        });

        List<AudioRecord> list = realm.where(AudioRecord.class).equalTo("id", currentDate.getTime()).findAll();
        return (list.size() > 0 ? list.get(0) : null);
    }

    public void saveDescriptionWithAudio(final AudioRecord audioRecord, final String description) {
        realm.executeTransaction(realm1 -> {
            audioRecord.setDescription(description);
            realm1.insertOrUpdate(audioRecord);
        });
    }

    public List<AudioRecord> getAllAudioRecords(Realm realm) {
        List<AudioRecord> list = realm.where(AudioRecord.class).findAll();
        return list;
    }

    public File[] getAudioFiles() {
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

    public void deleteAudioFile(String filename) {
        File file = new File(getAbsoluteFileName(filename));
        file.delete();
    }

    public void listAudioFiles() {
        File[] files = getAudioFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; ++i) {
                Log.d(TAG, files[i].getName());
            }
        }
    }


    // To fix "oops"
    public void createMissingAudioRecords() {
        boolean recordExists;
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
                    createAudioRecord(new Date(), file.getName());
                    Log.d(TAG, "created audio record for " + file.getName());
                }
            }
        }

    }

    public AudioRecord getAudioRecord(long id) {
        AudioRecord realmAudioRecord = realm.where(AudioRecord.class).equalTo(AudioRecord.FIELDS.id.name(), id).findFirst();
        AudioRecord nonRealmAudioRecord = realm.copyFromRealm(realmAudioRecord);
        return nonRealmAudioRecord;
    }

    public void updateAudioRecord(final AudioRecord audioRecord) {
        if (audioRecord.isChanged()) {
            // Asynchronously update objects on a background thread
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(audioRecord);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "Success: audioRecord updated");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.e(TAG, "Error:" + error.toString());
                }
            });

        }
    }

    public void deleteItemAsync(final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                delete(realm, id);
            }
        });
    }

    public void delete(Realm realm, long id) {
        AudioRecord audioRecord = realm.where(AudioRecord.class).equalTo(AudioRecord.FIELDS.id.name(), id).findFirst();
        // Otherwise it has been deleted already.
        if (audioRecord != null) {
            deleteAudioFile(audioRecord.getAudioFileName());
            audioRecord.deleteFromRealm();
        }
    }

}
