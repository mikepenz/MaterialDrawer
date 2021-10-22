package com.mikepenz.materialdrawer.app

import android.app.Application
import com.mikepenz.iconics.Iconics

/**
 * Created by mikepenz on 27.03.15.
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Iconics.init(this)
    }
}

