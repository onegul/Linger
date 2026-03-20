package app.linger.di

import android.content.Context
import app.linger.data.local.dao.ProximityIdMappingDao
import app.linger.data.remote.api.ProfileApi
import app.linger.proximity.BroadcastController
import app.linger.proximity.ProximityIdResolver
import app.linger.proximity.ProximityScanner
import app.linger.proximity.RemoteProximityIdResolver
import app.linger.proximity.impl.BleBroadcastController
import app.linger.proximity.impl.BleProximityScanner
import app.linger.proximity.impl.EphemeralIdProvider
import app.linger.proximity.impl.RotatingEphemeralIdProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProximityModule {
    @Provides
    @Singleton
    fun provideEphemeralIdProvider(): EphemeralIdProvider =
        RotatingEphemeralIdProvider(
            stableUserId = "me",        // TODO: replace once auth exists
            rotationMinutes = 15
        )

    @Provides
    @Singleton
    fun provideProximityIdResolver(
        mappingDao: ProximityIdMappingDao,
        profileApi: ProfileApi
    ): ProximityIdResolver =
        RemoteProximityIdResolver(mappingDao, profileApi)

    @Provides
    @Singleton
    fun provideProximityScanner(@ApplicationContext context: Context): ProximityScanner =
        BleProximityScanner(context)

    @Provides
    @Singleton
    fun provideBroadcastController(
        @ApplicationContext context: Context,
        ephemeralIdProvider: EphemeralIdProvider
    ): BroadcastController = BleBroadcastController(context, ephemeralIdProvider)
}