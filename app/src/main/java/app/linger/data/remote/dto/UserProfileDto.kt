package app.linger.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class UserProfileDto(
    @Json(name = "id") val id: String,
    @Json(name = "alias") val alias: String? = null,
    @Json(name = "life_tags") val lifeTags: List<String>? = null,
    @Json(name = "current_reading") val currentReading: List<String>? = null,
    @Json(name = "past_reading") val pastReading: List<String>? = null,
    @Json(name = "activities") val activities: List<String>? = null,
    @Json(name = "music") val music: List<String>? = null,
    @Json(name = "foods") val foods: List<String>? = null,
    @Json(name = "videos") val videos: List<String>? = null,
    @Json(name = "is_venue") val isVenue: Boolean = false
)
