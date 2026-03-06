package app.linger.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ChatThreadDto(
    @Json(name = "id") val id: String,
    @Json(name = "peer_id") val peerId: String,
    @Json(name = "mode") val mode: String,
    @Json(name = "last_message_preview") val lastMessagePreview: String? = null,
    @Json(name = "last_updated_at") val lastUpdatedAt: Long,
    @Json(name = "is_muted") val isMuted: Boolean = false
)
