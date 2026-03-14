package app.linger.proximity.model

import kotlin.time.Instant

/**
 * A single detection of a broadcasting target near the device.
 */
data class ProximityHit(
    val targetId: String,           // userId or venueId (or ephemeral that maps to it)
    val type: ProximityTargetType,
    val distanceMeters: Double?,    // approximate; may be null
    val rssi: Int?,                 // raw signal strength if available
    val firstSeenAt: Instant,
    val lastSeenAt: Instant
)

enum class ProximityTargetType {
    USER,
    VENUE
}