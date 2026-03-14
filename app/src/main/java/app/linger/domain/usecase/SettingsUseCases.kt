package app.linger.domain.usecase

import app.linger.data.repository.settings.SettingsRepository
import app.linger.domain.model.BroadcastSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBroadcastSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<BroadcastSettings> = settingsRepository.broadcastSettings
}

class UpdateBroadcastSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(block: (BroadcastSettings) -> BroadcastSettings) {
        settingsRepository.updateBroadcastSettings(block)
    }
}