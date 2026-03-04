package app.linger.domain.model

import kotlin.time.Instant

data class Encounter(
    val id: String,
    val otherId: String,
    val type: EncounterType,
    val firstSeenAt: Instant,
    val lastSeenAt: Instant,
    val approximateDistanceMeters: Double?,
    val resonance: ResonanceScore?,
    val wasLocalChatStarted: Boolean,
    val isRemoteFriend: Boolean
)

enum class EncounterType {
    USER,
    VENUE
}