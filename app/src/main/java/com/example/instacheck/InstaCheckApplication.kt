package com.example.instacheck

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class InstaCheckApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}