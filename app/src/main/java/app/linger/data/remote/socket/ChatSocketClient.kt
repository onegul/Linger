package app.linger.data.remote.socket

import app.linger.data.remote.dto.ChatMessageDto
import app.linger.data.remote.dto.ChatThreadDto
import kotlinx.coroutines.flow.Flow

interface ChatSocketClient {
    val events: Flow<ChatSocketEvent>

    fun connect()

    fun disconnect()

    fun sendMessage(threadId: String, content: String)
}

sealed interface ChatSocketEvent {
    data class MessageReceived(val message: ChatMessageDto) : ChatSocketEvent
    data class ThreadUpdated(val thread: ChatThreadDto) : ChatSocketEvent
}