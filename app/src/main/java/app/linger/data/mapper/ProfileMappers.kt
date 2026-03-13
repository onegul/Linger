package app.linger.data.mapper

import app.linger.data.local.entity.UserProfileEntity
import app.linger.data.remote.dto.UserProfileDto
import app.linger.domain.model.Profile

fun UserProfileEntity.toDomain(): Profile =
    Profile(
        id = id,
        alias = alias,
        lifeTags = lifeTags,
        currentReading = currentReading,
        pastReading = pastReading,
        activities = activities,
        music = music,
        foods = foods,
        videos = videos,
        isVenue = isVenue
    )

fun Profile.toEntity(): UserProfileEntity =
    UserProfileEntity(
        id = id,
        alias = alias,
        lifeTags = lifeTags,
        currentReading = currentReading,
        pastReading = pastReading,
        activities = activities,
        music = music,
        foods = foods,
        videos = videos,
        isVenue = isVenue
    )

fun UserProfileDto.toDomain(): Profile =
    Profile(
        id = id,
        alias = alias,
        lifeTags = lifeTags ?: emptyList(),
        currentReading = currentReading ?: emptyList(),
        pastReading = pastReading ?: emptyList(),
        activities = activities ?: emptyList(),
        music = music ?: emptyList(),
        foods = foods ?: emptyList(),
        videos = videos ?: emptyList(),
        isVenue = isVenue
    )