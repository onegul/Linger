package app.linger.domain.model

data class ResonanceScore(
    val value: Double,          // 0.0 ... 1.0
    val level: ResonanceLevel
)

enum class ResonanceLevel {
    LOW,
    MEDIUM,
    HIGH
}