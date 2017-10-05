package com.fisincorporated.speechtotext.audio.utils;

import android.content.Context;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.data.AudioRecord;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;

public class AudioRecordUtils {

    private static final String TAG = AudioRecordUtils.class.getSimpleName();

    private static final String fileSeparator = "/";

    private Context context;

    private Realm realm;

    @Inject
    public AudioRecordUtils(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    public void closeRealm() {
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    public OrderedRealmCollection<AudioRecord> getOrderedRealmCollection() {
        return realm.where(AudioRecord.class).notEqualTo(AudioRecord.FIELDS.id.name(), 0).findAllSorted(AudioRecord.FIELDS.id.name(), Sort.DESCENDING);
    }

    public String getAudioDirectoryPath() {
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
                audioRecord.setRecordDateTime(currentDate.toString());
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
        File file;
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; ++i) {
                file = files[i];
                Log.d(TAG, file.getAbsolutePath() + " " + file.getName() + " " + file.length());
            }
        }
    }


    // To fix "oops"
    public void createMissingAudioRecords() {
        boolean recordExists;
        File[] files = getAudioFiles();
        Log.d(TAG, "Number of audio files found:" + files.length);
        List<AudioRecord> list = getAllAudioRecords(realm);
        if (files != null && files.length > 0) {
            for (File file : files) {
                recordExists = false;
                for (AudioRecord audioRecord : list) {
                    if (file.getName().equals(audioRecord.getAudioFileName())) {
                        recordExists = true;
                        if (audioRecord.getRecordDateTime().isEmpty()) {
                            AudioRecord clonedAudioRecord = realm.copyFromRealm(audioRecord);
                            clonedAudioRecord.setRecordDateTime(new Date(file.lastModified()).toString());
                            updateAudioRecordAsync(clonedAudioRecord);
                        }
                        break;
                    }
                }
                if (!recordExists) {
                    createAudioRecord(new Date(file.lastModified()), file.getName());
                    Log.d(TAG, "created audio record for " + file.getName());
                    break;
                }
            }
        }
    }

    public AudioRecord getRealmAudioRecord(long id) {
        return realm.where(AudioRecord.class).equalTo(AudioRecord.FIELDS.id.name(), id).findFirst();
    }

    public AudioRecord getNonRealmAudioRecord(long id) {
        AudioRecord realmAudioRecord = getRealmAudioRecord(id);
        return copyToNonRealmAudioRecord(realmAudioRecord);
    }

    public AudioRecord copyToNonRealmAudioRecord(AudioRecord realmAudioRecord) {
        AudioRecord nonRealmAudioRecord = null;
        if (realmAudioRecord != null) {
            nonRealmAudioRecord = realm.copyFromRealm(realmAudioRecord);
        }
        return nonRealmAudioRecord;

    }

    // Should use for updates on UI thread
    public void updateAudioRecordAsync(final AudioRecord audioRecord) {
        // Asynchronously update objects on a background thread
        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        bgRealm.copyToRealmOrUpdate(audioRecord);
                    }
                }
                , new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        realm.refresh();
                        Log.e(TAG, "Success: audioRecord updated: JobId:" + audioRecord.getXlatJobNumber()
                                + " text:" + audioRecord.getSpeechToTextTranslation());
                    }
                }
                , new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error:" + error.toString());
                    }
                });

    }

    // Be careful using this as may block if updates being done on both UI and background threads.
    public void updateAudioRecordSync(final AudioRecord audioRecord) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(audioRecord);
        realm.commitTransaction();
    }

    public void deleteAudioRecord(AudioRecord audioRecord) {
        deleteAudioFile(audioRecord.getAudioFileName());
        deleteAudioRecordByIdAsync(audioRecord.getId());
    }

    public void deleteAudioRecordByIdAsync(final long id) {
        deleteAudioRecordByIdAsync(realm, id);
    }

    public void deleteAudioRecordByIdAsync(Realm realm, final long id) {
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
