package com.fisincorporated.speechtotext.ui;



import com.fisincorporated.speechtotext.application.ViewModelLifeCycle;

import io.realm.Realm;



public class AudioBaseViewModel implements ViewModelLifeCycle {

    protected Realm realm;


    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        if (realm != null) {
            realm.close();
        }
    }
}
