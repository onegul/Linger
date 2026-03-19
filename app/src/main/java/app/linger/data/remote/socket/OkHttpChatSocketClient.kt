package app.linger.data.remote.socket

import app.linger.data.remote.dto.ChatMessageDto
import app.linger.data.remote.dto.ChatThreadDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.IOException

private const val CHAT_WS_URL = "wss://api.linger.example/chat"

class OkHttpChatSocketClient(
    private val okHttpClient: OkHttpClient,
    moshi: Moshi
) : ChatSocketClient {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _events = MutableSharedFlow<ChatSocketEvent>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val events: Flow<ChatSocketEvent> = _events

    private var webSocket: WebSocket? = null

    private val mapAdapter = moshi.adapter<Map<String, Any?>>(
        Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            Any::class.java
        )
    )

    private val messageAdapter = moshi.adapter(ChatMessageDto::class.java)
    private val threadAdapter = moshi.adapter(ChatThreadDto::class.java)

    override fun connect() {
        if (webSocket != null) return

        val request = Request.Builder()
            .url(CHAT_WS_URL)
            .build()

        webSocket = okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    handleIncoming(text)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    // TODO: retries / logging will be added later
                }
            }
        )
    }

    override fun disconnect() {
        webSocket?.close(1000, "client closing")
        webSocket = null
    }

    override fun sendMessage(threadId: String, content: String) {
        val payload = mapOf(
            "type" to "send",
            "threadId" to threadId,
            "content" to content
        )
        val json = mapAdapter.toJson(payload)
        webSocket?.send(json)
    }

    private fun handleIncoming(raw: String) {
        scope.launch {
            try {
                val map = mapAdapter.fromJson(raw) ?: return@launch
                when (map["type"]) {
                    "message" -> {
                        val msg = messageAdapter.fromJson(raw) ?: return@launch
                        _events.emit(ChatSocketEvent.MessageReceived(msg))
                    }

                    "thread_update" -> {
                        val thread = threadAdapter.fromJson(raw) ?: return@launch
                        _events.emit(ChatSocketEvent.ThreadUpdated(thread))
                    }
                }
            } catch (_: IOException) {
                // ignore for now
            }
        }
    }
}