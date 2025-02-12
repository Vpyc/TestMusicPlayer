package com.vpyc.testmusicplayer.di

import dagger.Module

@Module(includes = [
    NetworkModule::class,
    DataModule::class,
    ViewModelModule::class])
class AppModule