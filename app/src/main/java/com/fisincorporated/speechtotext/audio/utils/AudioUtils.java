package com.fisincorporated.speechtotext.audio.utils;

import android.content.Context;

public class AudioUtils {

    public static String getAbsoluteFileName(Context context, String filename) {
        return context.getFilesDir() + "/" +  filename;
    }
}
