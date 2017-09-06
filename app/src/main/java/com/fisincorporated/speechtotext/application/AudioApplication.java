package com.fisincorporated.speechtotext.application;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fisincorporated.speechtotext.audio.utils.AudioRecordUtils;
import com.fisincorporated.speechtotext.dagger.application.DaggerApplicationComponent;
import com.google.common.io.BaseEncoding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;

public class AudioApplication extends Application implements HasDispatchingActivityInjector {
    private static final String TAG = AudioApplication.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    AudioRecordUtils audioRecordsUtils;

    private StorageReference storageRef;

    @Override
    public void onCreate() {
        super.onCreate();
        createDaggerInjections();
        audioRecordsUtils.listAudioFiles();
        audioRecordsUtils.createMissingAudioRecords();
        storageRef = FirebaseStorage.getInstance().getReference();
        loadAudioConverter();
        displayPackageAndSigniture();
    }

    private void displayPackageAndSigniture() {
        String packageName = getPackageName();
        Log.d(TAG, "Package name:" + getPackageName());
        // String sig = getSignature(getPackageManager(), packageName);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null
                || packageInfo.signatures == null
                || packageInfo.signatures.length == 0
                || packageInfo.signatures[0] == null) {
          //  return null;
        }
        //return signatureDigest(packageInfo.signatures[0]);
        Log.d(TAG, "Package signature:" + packageInfo.signatures[0]);
    }

    protected void createDaggerInjections() {
        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    private void loadAudioConverter() {
        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "AndroidAudioConverger loaded successfully");
            }

            @Override
            public void onFailure(Exception error) {
                Log.d(TAG, "AndroidAudioConverter NOT LOADED!");
            }
        });
    }

    public static String getSignature(@NonNull PackageManager pm, @NonNull String packageName) {
               try {
                        PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                         if (packageInfo == null
                                       || packageInfo.signatures == null
                                         || packageInfo.signatures.length == 0
                                        || packageInfo.signatures[0] == null) {
                                return null;
                            }
                        return signatureDigest(packageInfo.signatures[0]);
                    } catch (PackageManager.NameNotFoundException e) {
                        return null;
                   }
    }

    private static String signatureDigest(Signature sig) {
        byte[] signature = sig.toByteArray();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(signature);
            return BaseEncoding.base16().lowerCase().encode(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


}
