package app.linger.domain.usecase

import app.linger.core.util.Result
import app.linger.data.repository.chat.ChatRepository
import app.linger.domain.model.ChatMessage
import app.linger.domain.model.ChatMode
import app.linger.domain.model.ChatThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatThreadsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatThread>> = chatRepository.observeThreads()
}

class ObserveChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(threadId: String): Flow<List<ChatMessage>> =
        chatRepository.observeMessages(threadId)
}

class RefreshChatThreadsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke() {
        chatRepository.refreshThreads()
    }
}

class RefreshChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(threadId: String) {
        chatRepository.refreshMessages(threadId)
    }
}

class SendMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(threadId: String, context: String): Result<ChatMessage> =
        chatRepository.sendMessage(threadId, context)
}

class CreateThreadUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(peerId: String, mode: ChatMode): Result<ChatThread> =
        chatRepository.createThread(peerId, mode)
}