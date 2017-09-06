package com.fisincorporated.speechtotext.audio.data;


import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class AudioRecordMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0) {
            schema.get(AudioRecord.AUDIO_RECORD)
                    .addPrimaryKey(AudioRecord.FIELDS.id.name());
            oldVersion++;
        }

        if (oldVersion == 1) {
            // fix field name
            schema.get(AudioRecord.AUDIO_RECORD).renameField("speachToTextTranslation","speechToTextTranslation");
            // add translation job number field
            schema.get(AudioRecord.AUDIO_RECORD).addField("xlatJobNumber", int.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setInt("xlatJobNumber", 0);
                        }
                    });
            // add translation status field
            schema.get(AudioRecord.AUDIO_RECORD).addField("speechToTextStatus", int.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setInt("speechToTextStatus", 0);
                        }
                    });
            oldVersion++;
        }
    }

}
