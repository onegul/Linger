package app.linger.data.repository.settings

import app.linger.domain.model.BroadcastSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val broadcastSettings: Flow<BroadcastSettings>

    suspend fun updateBroadcastSettings(block: (BroadcastSettings) -> BroadcastSettings)
}