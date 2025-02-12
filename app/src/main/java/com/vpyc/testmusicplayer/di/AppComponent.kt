package com.vpyc.testmusicplayer.di

import com.vpyc.testmusicplayer.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
}