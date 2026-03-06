package app.linger.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ChatMessageDto(
    @Json(name = "id") val id: String,
    @Json(name = "thread_id") val threadId: String,
    @Json(name = "sender") val sender: String,
    @Json(name = "content") val content: String,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "status") val status: String
)
