package app.linger.data.mapper

import app.linger.data.local.entity.EncounterEntity
import app.linger.data.remote.dto.EncounterDto
import app.linger.domain.model.Encounter
import app.linger.domain.model.EncounterType
import app.linger.domain.model.ResonanceLevel
import app.linger.domain.model.ResonanceScore
import kotlin.time.Instant

fun EncounterEntity.toDomain(): Encounter =
    Encounter(
        id = id,
        otherId = otherId,
        type = type,
        firstSeenAt = firstSeenAt,
        lastSeenAt = lastSeenAt,
        approximateDistanceMeters = approximateDistanceMeters,
        resonance = if (resonanceValue != null && resonanceLevel != null)
            ResonanceScore(resonanceValue, resonanceLevel)
        else
            null,
        wasLocalChatStarted = wasLocalChatStarted,
        isRemoteFriend = isRemoteFriend
    )

fun Encounter.toEntity(): EncounterEntity =
    EncounterEntity(
        id = id,
        otherId = otherId,
        type = type,
        firstSeenAt = firstSeenAt,
        lastSeenAt = lastSeenAt,
        approximateDistanceMeters = approximateDistanceMeters,
        resonanceValue = resonance?.value,
        resonanceLevel = resonance?.level,
        wasLocalChatStarted = wasLocalChatStarted,
        isRemoteFriend = isRemoteFriend
    )

fun EncounterDto.toEntity(): EncounterEntity =
    EncounterEntity(
        id = id,
        otherId = otherId,
        type = when (type.uppercase()) {
            "VENUE" -> EncounterType.VENUE
            else -> EncounterType.USER
        },
        firstSeenAt = Instant.fromEpochMilliseconds(firstSeenAt),
        lastSeenAt = Instant.fromEpochMilliseconds(lastSeenAt),
        approximateDistanceMeters = distanceMeters,
        resonanceValue = resonanceValue,
        resonanceLevel = resonanceLevel?.let { safeName ->
            runCatching { enumValueOf<ResonanceLevel>(safeName.uppercase()) }.getOrNull()
        },
        wasLocalChatStarted = wasLocalChatStarted,
        isRemoteFriend = isRemoteFriend
    )