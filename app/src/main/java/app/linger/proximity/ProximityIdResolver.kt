package app.linger.proximity

interface ProximityIdResolver {
    /**
     * Resolve scanned proximity ID to canonical profile/venue ID.
     * Returns null if unresolved.
     */
    suspend fun resolve(scannedId: String): String?
}

/**
 * Development resolver: assumes scanned ID is already canonical.
 * Replace with server-backed resolver when ephemeral mapping is implemented.
 */
class PassThroughProximityIdResolver : ProximityIdResolver {
    override suspend fun resolve(scannedId: String): String? = scannedId
}