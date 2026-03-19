package app.linger.di

import app.linger.data.remote.NetworkConfig
import app.linger.data.remote.api.ChatApi
import app.linger.data.remote.api.HistoryApi
import app.linger.data.remote.api.ProfileApi
import app.linger.data.remote.api.VenueApi
import app.linger.data.remote.socket.ChatSocketClient
import app.linger.data.remote.socket.OkHttpChatSocketClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideProfileApi(okHttpClient: OkHttpClient, moshi: Moshi): ProfileApi =
        NetworkConfig.createService(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideVenueApi(okHttpClient: OkHttpClient, moshi: Moshi): VenueApi =
        NetworkConfig.createService(VenueApi::class.java)

    @Provides
    @Singleton
    fun provideChatApi(okHttpClient: OkHttpClient, moshi: Moshi): ChatApi =
        NetworkConfig.createService(ChatApi::class.java)

    @Provides
    @Singleton
    fun provideHistoryApi(okHttpClient: OkHttpClient, moshi: Moshi): HistoryApi =
        NetworkConfig.createService(HistoryApi::class.java)

    @Provides
    @Singleton
    fun provideChatSocketClient(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): ChatSocketClient = OkHttpChatSocketClient(okHttpClient, moshi)
}