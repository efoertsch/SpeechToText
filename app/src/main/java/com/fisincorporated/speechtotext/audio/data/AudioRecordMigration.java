package com.fisincorporated.speechtotext.audio.data;


import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class AudioRecordMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0) {
            schema.get("AudioRecord")
                    .addPrimaryKey(AudioRecord.FIELDS.id.name());
            oldVersion++;
        }
    }

}
