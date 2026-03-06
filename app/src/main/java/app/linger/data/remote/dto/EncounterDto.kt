package app.linger.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class EncounterDto(
    @Json(name = "id") val id: String,
    @Json(name = "other_id") val otherId: String,
    @Json(name = "type") val type: String,
    @Json(name = "first_seen_at") val firstSeenAt: Long,
    @Json(name = "last_seen_at") val lastSeenAt: Long,
    @Json(name = "distance_meters") val distanceMeters: Double? = null,
    @Json(name = "resonance_value") val resonanceValue: Double? = null,
    @Json(name = "resonance_level") val resonanceLevel: String? = null,
    @Json(name = "was_local_chat_started") val wasLocalChatStarted: Boolean = false,
    @Json(name = "is_remote_friend") val isRemoteFriend: Boolean = false
)
