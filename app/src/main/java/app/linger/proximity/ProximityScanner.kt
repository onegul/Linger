package app.linger.proximity

import app.linger.proximity.model.ProximityHit
import kotlinx.coroutines.flow.Flow

interface ProximityScanner {
    /**
     * Emits a stream of current proximity hits (aggregated over time).
     * Implementation decides the refresh cadence (e.g., every few seconds).
     */
    val proximityHits: Flow<List<ProximityHit>>

    /**
     * Start scanning for nearby broadcasting users/venues.
     * Safe to call multiple times; no-op if already active.
     */
    fun startScanning()

    /**
     * Stop scanning to save battery.
     */
    fun stopScanning()
}