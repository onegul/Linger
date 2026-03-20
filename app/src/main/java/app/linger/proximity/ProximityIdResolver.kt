package app.linger.proximity

interface ProximityIdResolver {
    /**
     * Resolve scanned proximity ID to canonical profile/venue ID.
     * Returns null if unresolved.
     */
    suspend fun resolve(scannedId: String): String?
}