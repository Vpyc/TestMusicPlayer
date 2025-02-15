package com.vpyc.testmusicplayer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vpyc.testmusicplayer.player.PlayerViewModel
import com.vpyc.testmusicplayer.tracklist.LocalTrackListViewModel
import com.vpyc.testmusicplayer.tracklist.OnlineTrackListViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(OnlineTrackListViewModel::class)
    abstract fun bindOnlineTrackListViewModel(viewModel: OnlineTrackListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocalTrackListViewModel::class)
    abstract fun bindLocalTrackListViewModel(viewModel: LocalTrackListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)