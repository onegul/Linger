package app.linger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.linger.data.local.entity.EncounterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EncounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(encounters: List<EncounterEntity>)

    @Query("SELECT * FROM encounters ORDER BY lastSeenAt DESC")
    fun observeAll(): Flow<List<EncounterEntity>>

    @Query("SELECT * FROM encounters WHERE isRemoteFriend = 1 ORDER BY lastSeenAt DESC")
    fun observeRemoteFriends(): Flow<List<EncounterEntity>>

    @Query("UPDATE encounters SET wasLocalChatStarted = :started WHERE otherId = :otherId")
    suspend fun setWasLocalChatStarted(otherId: String, started: Boolean): Int

    @Query("UPDATE encounters SET isRemoteFriend = :remote WHERE otherId = :otherId")
    suspend fun setIsRemoteFriend(otherId: String, remote: Boolean): Int
}