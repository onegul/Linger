package app.linger.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linger.domain.model.BroadcastSettings
import app.linger.domain.model.Discoverability
import app.linger.domain.usecase.ObserveBroadcastSettingsUseCase
import app.linger.domain.usecase.UpdateBroadcastSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeBroadcastSettings: ObserveBroadcastSettingsUseCase,
    private val updateBroadcastSettings: UpdateBroadcastSettingsUseCase,
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> =
        observeBroadcastSettings()
            .map { settings -> SettingsUiState(settings = settings) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState(
                    settings = BroadcastSettings(
                        discoverability = Discoverability.ALL,
                        showLifeToPeople = true
                    )
                )
            )

    fun setShowLifeToPeople(enabled: Boolean) {
        viewModelScope.launch {
            updateBroadcastSettings { it.copy(showLifeToPeople = enabled) }
        }
    }
}