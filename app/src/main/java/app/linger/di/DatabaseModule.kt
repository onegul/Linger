package app.linger.di

import android.content.Context
import androidx.room.Room
import app.linger.data.local.LingerDatabase
import app.linger.data.local.dao.ChatDao
import app.linger.data.local.dao.EncounterDao
import app.linger.data.local.dao.ProximityIdMappingDao
import app.linger.data.local.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LingerDatabase =
        Room.databaseBuilder(context, LingerDatabase::class.java, "linger.db").build()

    @Provides
    fun provideUserProfileDao(db: LingerDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideEncounterDao(db: LingerDatabase): EncounterDao = db.encounterDao()

    @Provides
    fun provideChatDao(db: LingerDatabase): ChatDao = db.chatDao()

    @Provides
    fun provideProximityIdMappingDao(db: LingerDatabase): ProximityIdMappingDao =
        db.proximityIdMappingDao()
}