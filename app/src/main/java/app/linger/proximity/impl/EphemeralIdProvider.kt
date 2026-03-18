package app.linger.proximity.impl

import java.security.MessageDigest

interface EphemeralIdProvider {
    fun currentEphemeralId(nowMillis: Long = System.currentTimeMillis()): String
}

class RotatingEphemeralIdProvider(
    private val stableUserId: String,
    private val rotationMinutes: Int = 15
) : EphemeralIdProvider {
    override fun currentEphemeralId(nowMillis: Long): String {
        val bucket = (nowMillis / (rotationMinutes * 60_000L))
        val input = "$stableUserId:$bucket"
        return sha256Hex(input).take(16) // short id for BLE payload
    }
}

private fun sha256Hex(input: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}