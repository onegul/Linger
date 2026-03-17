package app.linger.di

import app.linger.data.remote.NetworkConfig
import app.linger.data.remote.api.ChatApi
import app.linger.data.remote.api.HistoryApi
import app.linger.data.remote.api.ProfileApi
import app.linger.data.remote.api.VenueApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideProfileApi(): ProfileApi =
        NetworkConfig.createService(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideVenueApi(): VenueApi =
        NetworkConfig.createService(VenueApi::class.java)

    @Provides
    @Singleton
    fun provideChatApi(): ChatApi =
        NetworkConfig.createService(ChatApi::class.java)

    @Provides
    @Singleton
    fun provideHistoryApi(): HistoryApi =
        NetworkConfig.createService(HistoryApi::class.java)
}