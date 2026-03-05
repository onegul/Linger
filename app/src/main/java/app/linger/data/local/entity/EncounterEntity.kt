package app.linger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.linger.domain.model.EncounterType
import app.linger.domain.model.ResonanceLevel
import kotlin.time.Instant

@Entity(tableName = "encounters")
data class EncounterEntity(
    @PrimaryKey val id: String,
    val otherId: String,
    val type: EncounterType,
    val firstSeenAt: Instant,
    val lastSeenAt: Instant,
    val approximateDistanceMeters: Double?,
    val resonanceValue: Double?,
    val resonanceLevel: ResonanceLevel?,
    val wasLocalChatStarted: Boolean,
    val isRemoteFriend: Boolean
)