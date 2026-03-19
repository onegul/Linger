package app.linger.data.local

import androidx.room.TypeConverter
import app.linger.domain.model.ChatMode
import app.linger.domain.model.Discoverability
import app.linger.domain.model.EncounterType
import app.linger.domain.model.MessageSender
import app.linger.domain.model.MessageStatus
import app.linger.domain.model.ResonanceLevel
import kotlin.time.Instant

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(separator = "|")

    @TypeConverter
    fun toStringList(data: String?): List<String> =
        data?.takeIf { it.isNotEmpty() }?.split("|") ?: emptyList()

    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilliseconds()

    @TypeConverter
    fun toInstant(millis: Long?): Instant? = millis?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun fromResonanceLevel(level: ResonanceLevel?): String? = level?.name

    @TypeConverter
    fun toResonanceLevel(name: String?): ResonanceLevel? =
        name?.let { enumValueOf<ResonanceLevel>(it) }

    @TypeConverter
    fun fromMessageSender(sender: MessageSender): String = sender.name

    @TypeConverter
    fun toMessageSender(name: String): MessageSender = enumValueOf(name)

    @TypeConverter
    fun fromMessageStatus(status: MessageStatus): String = status.name

    @TypeConverter
    fun toMessageStatus(name: String): MessageStatus = enumValueOf(name)

    @TypeConverter
    fun fromDiscoverability(discoverability: Discoverability): String = discoverability.name

    @TypeConverter
    fun toDiscoverability(name: String): Discoverability = enumValueOf(name)

    @TypeConverter
    fun fromChatMode(mode: ChatMode): String = mode.name

    @TypeConverter
    fun toChatMode(name: String): ChatMode = enumValueOf(name)

    @TypeConverter
    fun fromEncounterType(type: EncounterType): String = type.name

    @TypeConverter
    fun toEncounterType(name: String): EncounterType = enumValueOf(name)
}