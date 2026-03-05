package app.linger.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.linger.data.local.dao.ChatDao
import app.linger.data.local.dao.EncounterDao
import app.linger.data.local.dao.UserProfileDao
import app.linger.data.local.entity.ChatMessageEntity
import app.linger.data.local.entity.ChatThreadEntity
import app.linger.data.local.entity.EncounterEntity
import app.linger.data.local.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        EncounterEntity::class,
        ChatThreadEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LingerDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao

    abstract fun encounterDao(): EncounterDao

    abstract fun chatDao(): ChatDao
}