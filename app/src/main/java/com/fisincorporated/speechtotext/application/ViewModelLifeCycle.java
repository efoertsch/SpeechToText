package com.fisincorporated.speechtotext.application;

public interface ViewModelLifeCycle {
        void onResume();
        void onPause();
        void onStop();
        void onDestroy();
}
