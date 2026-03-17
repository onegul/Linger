package app.linger.di

import app.linger.proximity.BroadcastController
import app.linger.proximity.FakeBroadcastController
import app.linger.proximity.FakeProximityScanner
import app.linger.proximity.ProximityScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProximityModule {
    @Provides
    @Singleton
    fun provideProximityScanner(): ProximityScanner = FakeProximityScanner()

    @Provides
    @Singleton
    fun provideBroadcastController(): BroadcastController = FakeBroadcastController()
}