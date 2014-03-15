package com.tundem.holokitkatdrawer;


import android.app.Application;

import com.tundem.holokitkatdrawer.util.Cfg;
import com.tundem.holokitkatdrawer.util.UIUtils;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        UIUtils.init(getApplicationContext(), Cfg.accentColor, Cfg.accentSecondaryColor, true, true, true, true);
    }
}
