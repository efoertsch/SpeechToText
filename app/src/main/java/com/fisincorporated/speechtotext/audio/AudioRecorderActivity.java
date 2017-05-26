package com.fisincorporated.speechtotext.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.dagger.ActivityComponent;
import com.fisincorporated.speechtotext.dagger.ActivityModule;
import com.fisincorporated.speechtotext.dagger.DaggerActivityComponent;

import javax.inject.Inject;

public class AudioRecorderActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mediaFileName = null;

    @Inject
    public AudioRecorderViewModel audioRecorderViewModel;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComponent activityComponent;
        activityComponent = DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build();
        activityComponent.inject(this);
        setContentView(R.layout.activity_audio_recorder);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        audioRecorderViewModel.setView((ViewGroup) findViewById(android.R.id.content));

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }
}