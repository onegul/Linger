package app.linger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val alias: String?,
    val lifeTags: List<String>,
    val currentReading: List<String>,
    val pastReading: List<String>,
    val activities: List<String>,
    val music: List<String>,
    val foods: List<String>,
    val videos: List<String>,
    val isVenue: Boolean
)
