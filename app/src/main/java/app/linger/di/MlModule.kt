package app.linger.di

import app.linger.ml.ResonanceEngine
import app.linger.ml.SimpleResonanceEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MlModule {
    @Provides
    @Singleton
    fun provideResonanceEngine(): ResonanceEngine = SimpleResonanceEngine()
}