package app.linger.proximity

import app.linger.domain.model.BroadcastSettings
import kotlinx.coroutines.flow.Flow

/**
 * Controls whether this device is advertising its presence and enforces broadcast rules.
 */
interface BroadcastController {
    /**
     * Current broadcast state (ON/OFF) as seen by the controller.
     */
    val state: Flow<BroadcastState>

    /**
     * Attempt to enable broadcasting according to current settings.
     * Implementation may fail silently if permissions are missing.
     */
    suspend fun enableBroadcasting(settings: BroadcastSettings)

    /**
     * Disable broadcasting.
     */
    suspend fun disableBroadcasting()
}

enum class BroadcastState {
    OFF,
    ON
}