package app.linger.ui.chat

import app.linger.domain.model.ChatMessage
import app.linger.domain.model.ChatMode
import app.linger.domain.model.ChatThread

data class ChatListUiState(
    val isLoading: Boolean,
    val threads: List<ChatThread>
)

data class ChatUiState(
    val threadId: String,
    val mode: ChatMode,
    val title: String,
    val messages: List<ChatMessage>,
    val sending: Boolean
)