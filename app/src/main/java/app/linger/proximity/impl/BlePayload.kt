package app.linger.proximity.impl

import app.linger.proximity.model.ProximityTargetType
import java.nio.charset.StandardCharsets

private const val TYPE_USER: Byte = 0x01
private const val TYPE_VENUE: Byte = 0x02

data class DecodePayload(
    val targetId: String,
    val type: ProximityTargetType
)

fun encodePayload(
    type: ProximityTargetType,
    ephemeralId: String
): ByteArray {
    val idBytes = ephemeralId.toByteArray(StandardCharsets.UTF_8)
    val payload = ByteArray(1 + idBytes.size.coerceAtMost(18))  // Keep under 20 bytes typical
    payload[0] = when (type) {
        ProximityTargetType.USER -> TYPE_USER
        ProximityTargetType.VENUE -> TYPE_VENUE
    }
    System.arraycopy(idBytes, 0, payload, 1, payload.size - 1)
    return payload
}

fun decodePayload(bytes: ByteArray?): DecodePayload? {
    if (bytes == null || bytes.isEmpty()) return null
    val type = when (bytes[0]) {
        TYPE_USER -> ProximityTargetType.USER
        TYPE_VENUE -> ProximityTargetType.VENUE
        else -> return null
    }
    val id = if (bytes.size > 1)
        String(bytes, 1, bytes.size - 1, StandardCharsets.UTF_8)
    else
        return null

    return DecodePayload(targetId = id, type = type)
}