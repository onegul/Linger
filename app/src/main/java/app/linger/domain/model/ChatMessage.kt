package app.linger.domain.model

data class ChatMessage(
    val id: String,
    val threadId: String,
    val sender: MessageSender,
    val content: String,
    val timestampMillis: Long,
    val status: MessageStatus
)

enum class MessageSender {
    SELF,
    OTHER
}

enum class MessageStatus {
    PENDING,
    SENT,
    DELIVERED,
    FAILED
}