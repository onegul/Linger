package app.linger.data.repository.settings

import app.linger.data.local.settings.SettingsDataStore
import app.linger.domain.model.BroadcastSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {
    override val broadcastSettings: Flow<BroadcastSettings> =
        settingsDataStore.broadcastSettings

    override suspend fun updateBroadcastSettings(block: (BroadcastSettings) -> BroadcastSettings) {
        settingsDataStore.updateBroadcastSettings(block)
    }
}