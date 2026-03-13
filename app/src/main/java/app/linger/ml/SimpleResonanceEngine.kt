package app.linger.ml

import app.linger.domain.model.Profile
import app.linger.domain.model.ResonanceLevel
import app.linger.domain.model.ResonanceScore

class SimpleResonanceEngine : ResonanceEngine {
    override fun computeResonance(self: Profile, other: Profile): ResonanceScore {
        val selfFeatures = self.toEssenceFeatures()
        val otherFeatures = other.toEssenceFeatures()

        val tagScore = jaccardSimilarity(selfFeatures.tags, otherFeatures.tags)
        val readingScore = jaccardSimilarity(selfFeatures.reading, otherFeatures.reading)
        val activitiesScore = jaccardSimilarity(selfFeatures.activities, otherFeatures.activities)
        val musicScore = jaccardSimilarity(selfFeatures.music, otherFeatures.music)
        val foodsScore = jaccardSimilarity(selfFeatures.foods, otherFeatures.foods)
        val videosScore = jaccardSimilarity(selfFeatures.videos, otherFeatures.videos)

        // Simple weighted sum; ll be tuned later.
        val value =
            0.30 * tagScore +
                    0.20 + readingScore +
                    0.20 + activitiesScore +
                    0.15 + musicScore +
                    0.10 + foodsScore +
                    0.05 + videosScore

        val level = when {
            value >= 0.7 -> ResonanceLevel.HIGH
            value >= 0.35 -> ResonanceLevel.MEDIUM
            else -> ResonanceLevel.LOW
        }

        return ResonanceScore(value = value.coerceIn(0.0, 1.0), level = level)
    }
}