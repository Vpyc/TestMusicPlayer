package com.vpyc.testmusicplayer.di

import com.vpyc.testmusicplayer.data.ChartRepository
import com.vpyc.testmusicplayer.data.ChartRepositoryImpl
import com.vpyc.testmusicplayer.data.TrackRepository
import com.vpyc.testmusicplayer.data.TrackRepositoryImpl
import com.vpyc.testmusicplayer.retrofit.ChartApi
import com.vpyc.testmusicplayer.retrofit.SearchApi
import com.vpyc.testmusicplayer.retrofit.TrackApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideChartRepository(chartApi: ChartApi, searchApi: SearchApi): ChartRepository {
        return ChartRepositoryImpl(chartApi, searchApi)
    }
    @Provides
    @Singleton
    fun provideTrackRepository(trackApi: TrackApi): TrackRepository {
        return TrackRepositoryImpl(trackApi)
    }
}