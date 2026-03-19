package app.linger.data.repository.chat

import app.linger.core.util.DispatcherProvider
import app.linger.core.util.Result
import app.linger.data.local.dao.ChatDao
import app.linger.data.mapper.toDomain
import app.linger.data.mapper.toEntity
import app.linger.data.remote.api.ChatApi
import app.linger.data.remote.api.CreateThreadBody
import app.linger.data.remote.api.SendMessageBody
import app.linger.data.remote.socket.ChatSocketClient
import app.linger.data.remote.socket.ChatSocketEvent
import app.linger.domain.model.ChatMessage
import app.linger.domain.model.ChatMode
import app.linger.domain.model.ChatThread
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val chatApi: ChatApi,
    private val socketClient: ChatSocketClient,
    private val dispatchers: DispatcherProvider
) : ChatRepository {
    init {
        socketClient.connect()
        observeSocketEvents()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun observeSocketEvents() {
        GlobalScope.launch(dispatchers.io) {
            socketClient.events.collect { event ->
                when (event) {
                    is ChatSocketEvent.MessageReceived -> {
                        val entity = event.message.toEntity()
                        chatDao.upsertMessages(listOf(entity))
                    }

                    is ChatSocketEvent.ThreadUpdated -> {
                        val entity = event.thread.toEntity()
                        chatDao.upsertThreads(listOf(entity))
                    }
                }
            }
        }
    }

    override fun observeThreads(): Flow<List<ChatThread>> =
        chatDao.observeThreads().map { list -> list.map { it.toDomain() } }

    override fun observeMessages(threadId: String): Flow<List<ChatMessage>> =
        chatDao.observeMessages(threadId).map { list -> list.map { it.toDomain() } }

    override suspend fun refreshThreads() {
        withContext(dispatchers.io) {
            val remoteThreads = chatApi.getThreads()
            val entities = remoteThreads.map { it.toEntity() }
            chatDao.upsertThreads(entities)
        }
    }

    override suspend fun refreshMessages(threadId: String) {
        withContext(dispatchers.io) {
            val remoteMessages = chatApi.getMessages(threadId)
            val entities = remoteMessages.map { it.toEntity() }
            chatDao.upsertMessages(entities)
        }
    }

    override suspend fun sendMessage(threadId: String, context: String): Result<ChatMessage> =
        withContext(dispatchers.io) {
            try {
                val dto = chatApi.sendMessage(threadId, SendMessageBody(context))
                val entity = dto.toEntity()
                chatDao.upsertMessages(listOf(entity))
                Result.Success(entity.toDomain())
            } catch (t: Throwable) {
                Result.Error(t)
            }
        }

    override suspend fun createThread(peerId: String, mode: ChatMode): Result<ChatThread> =
        withContext(dispatchers.io) {
            try {
                val dto = chatApi.createThread(CreateThreadBody(peerId, mode.name.lowercase()))
                val entity = dto.toEntity()
                chatDao.upsertThreads(listOf(entity))
                Result.Success(entity.toDomain())
            } catch (t: Throwable) {
                Result.Error(t)
            }
        }

    override suspend fun setThreadMode(threadId: String, mode: ChatMode): Result<ChatThread> =
        withContext(dispatchers.io) {
            try {
                val now = System.currentTimeMillis()
                val updatedRows = chatDao.updateThreadMode(
                    threadId = threadId,
                    mode = mode,
                    lastUpdatedAtMillis = now
                )

                if (updatedRows == 0) {
                    return@withContext Result.Error(
                        throwable = IllegalStateException("Thread not found: $threadId")
                    )
                }

                val entity = chatDao.getThreadById(threadId)
                    ?: return@withContext Result.Error(
                        throwable = IllegalStateException("Thread not found after update: $threadId")
                    )

                Result.Success(entity.toDomain())
            } catch (t: Throwable) {
                Result.Error(t)
            }
        }
}