package app.linger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.linger.data.local.entity.ChatMessageEntity
import app.linger.data.local.entity.ChatThreadEntity
import app.linger.domain.model.ChatMode
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertThreads(threads: List<ChatThreadEntity>)

    @Query("SELECT * FROM chat_threads ORDER BY lastUpdatedAtMillis DESC")
    fun observeThreads(): Flow<List<ChatThreadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMessages(messages: List<ChatMessageEntity>)

    @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY timestampMillis ASC")
    fun observeMessages(threadId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_threads WHERE id = :threadId LIMIT 1")
    suspend fun getThreadById(threadId: String): ChatThreadEntity?

    @Query("UPDATE chat_threads SET mode = :mode, lastUpdatedAtMillis = :lastUpdatedAtMillis WHERE id = :threadId")
    suspend fun updateThreadMode(threadId: String, mode: ChatMode, lastUpdatedAtMillis: Long): Int
}