package app.linger.di

import app.linger.core.util.DefaultDispatcherProvider
import app.linger.core.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDispatcherProvide(): DispatcherProvider = DefaultDispatcherProvider()
}