package com.vpyc.testmusicplayer.app

import android.app.Application
import com.vpyc.testmusicplayer.di.AppComponent
import com.vpyc.testmusicplayer.di.DaggerAppComponent

class MyApp: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}
