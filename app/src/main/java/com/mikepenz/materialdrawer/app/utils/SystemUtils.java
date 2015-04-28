package com.mikepenz.materialdrawer.app.utils;

import android.content.res.Resources;

public class SystemUtils {
    public static int getScreenOrientation() {
        return Resources.getSystem().getConfiguration().orientation;
    }
}
