package app.linger.ml

import app.linger.domain.model.Profile

data class ProfileEssenceFeatures(
    val tags: Set<String>,
    val reading: Set<String>,
    val activities: Set<String>,
    val music: Set<String>,
    val foods: Set<String>,
    val videos: Set<String>
)

fun Profile.toEssenceFeatures(): ProfileEssenceFeatures =
    ProfileEssenceFeatures(
        tags = lifeTags.map(String::lowercase).toSet(),
        reading = (currentReading + pastReading).map(String::lowercase).toSet(),
        activities = activities.map(String::lowercase).toSet(),
        music = music.map(String::lowercase).toSet(),
        foods = foods.map(String::lowercase).toSet(),
        videos = videos.map(String::lowercase).toSet()
    )

fun jaccardSimilarity(a: Set<String>, b: Set<String>): Double {
    if (a.isEmpty() || b.isEmpty()) return 0.0
    val intersectionSize = a.intersect(b).size.toDouble()
    val unionSize = a.union(b).size.toDouble()
    return if (unionSize == 0.0) 0.0 else intersectionSize / unionSize
}