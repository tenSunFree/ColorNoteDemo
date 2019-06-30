package com.home.colornotedemo

import android.app.Application
import com.home.colornotedemo.main.model.ObjectBox
import com.jakewharton.threetenabp.AndroidThreeTen

class ColorNoteDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        ObjectBox.init(this)
    }
}
