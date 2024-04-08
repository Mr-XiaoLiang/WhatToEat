package org.lollipop.wte

import android.app.Application

class LApplication : Application() {

    companion object {
        lateinit var application: LApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}