package app.linger.di

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkModule {
    @Provides
    @Singleton
    fun provideWorkManagerConfig(
        workerFactory: HiltWorkerFactory
    ): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}