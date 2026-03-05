package app.linger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.linger.domain.model.ChatMode

@Entity(tableName = "chat_threads")
data class ChatThreadEntity(
    @PrimaryKey val id: String,
    val peerId: String,
    val mode: ChatMode,
    val lastMessagePreview: String?,
    val lastUpdatedAtMillis: Long,
    val isMuted: Boolean
)
