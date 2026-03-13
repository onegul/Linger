package app.linger.data.repository.chat

import app.linger.core.util.Result
import app.linger.domain.model.ChatMessage
import app.linger.domain.model.ChatMode
import app.linger.domain.model.ChatThread
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeThreads(): Flow<List<ChatThread>>

    fun observeMessages(threadId: String): Flow<List<ChatMessage>>

    suspend fun refreshThreads()

    suspend fun refreshMessages(threadId: String)

    suspend fun sendMessage(threadId: String, context: String): Result<ChatMessage>

    suspend fun createThread(peerId: String, mode: ChatMode): Result<ChatThread>
}