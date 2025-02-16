package com.vpyc.testmusicplayer.di

import android.content.Context
import com.vpyc.testmusicplayer.data.ChartRepository
import com.vpyc.testmusicplayer.data.ChartRepositoryImpl
import com.vpyc.testmusicplayer.data.ContentResolverHelper
import com.vpyc.testmusicplayer.data.TrackRepository
import com.vpyc.testmusicplayer.data.TrackRepositoryImpl
import com.vpyc.testmusicplayer.retrofit.ChartApi
import com.vpyc.testmusicplayer.retrofit.SearchApi
import com.vpyc.testmusicplayer.retrofit.TrackApi
import com.vpyc.testmusicplayer.service.MusicRepository
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
    @Provides
    @Singleton
    fun provideContentResolverHelper(context: Context): ContentResolverHelper {
        return ContentResolverHelper(context)
    }

    @Provides
    @Singleton
    fun provideMusicRepository(context: Context): MusicRepository {
        return MusicRepository(context)
    }

}