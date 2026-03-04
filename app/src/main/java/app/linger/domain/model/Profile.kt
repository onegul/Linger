package app.linger.domain.model

data class Profile(
    val id: String,
    val alias: String?,
    val lifeTags: List<String>,
    val currentReading: List<String>,
    val pastReading: List<String>,
    val activities: List<String>,
    val music: List<String>,
    val foods: List<String>,
    val videos: List<String>,
    val isVenue: Boolean = false
)
