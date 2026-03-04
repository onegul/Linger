package app.linger.domain.model

data class ChatThread(
    val id: String,
    val peerId: String,
    val mode: ChatMode,
    val lastMessagePreview: String?,
    val lastUpdatedAtMillis: Long,
    val isMuted: Boolean
)

enum class ChatMode {
    LOCAL,
    REMOTE
}