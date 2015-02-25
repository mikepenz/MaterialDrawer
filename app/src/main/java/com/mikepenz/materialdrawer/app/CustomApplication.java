package com.mikepenz.materialdrawer.app;


import android.app.Application;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //this is a feature of the Android-Iconics library. More about this library and the used Google Material Addon here: https://github.com/mikepenz/Android-Iconics
        Iconics.registerFont(new GoogleMaterial());
    }

}
