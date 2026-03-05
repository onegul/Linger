package app.linger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.linger.domain.model.MessageSender
import app.linger.domain.model.MessageStatus

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val threadId: String,
    val sender: MessageSender,
    val content: String,
    val timestampMillis: Long,
    val status: MessageStatus
)
