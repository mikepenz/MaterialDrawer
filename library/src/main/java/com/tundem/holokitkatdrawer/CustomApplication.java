package com.tundem.holokitkatdrawer;


import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.tundem.holokitkatdrawer.util.Cfg;
import com.tundem.holokitkatdrawer.util.UIUtils;

public class CustomApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        int accentColor = getResources().getColor(Cfg.accentColor);
        int accentSecondaryColor = Color.parseColor("#88" + Integer.toHexString(accentColor).toUpperCase().substring(2));
        int backgroundColor = getResources().getColor(Cfg.backgroundColor);

        UIUtils.init(getApplicationContext(), accentColor, accentSecondaryColor, backgroundColor, true, true, true, true);
    }
}
