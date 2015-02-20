package com.mikepenz.materialdrawer.app;


import android.app.Application;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Iconics.registerFont(new GoogleMaterial());
    }

}
