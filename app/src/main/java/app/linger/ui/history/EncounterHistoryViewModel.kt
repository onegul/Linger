package app.linger.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linger.domain.usecase.ObserveEncounterHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EncounterHistoryViewModel @Inject constructor(
    observeEncounterHistoryUseCase: ObserveEncounterHistoryUseCase
) : ViewModel() {
    val uiState: StateFlow<EncounterHistoryUiState> =
        observeEncounterHistoryUseCase()
            .map { encounters -> EncounterHistoryUiState(items = encounters) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EncounterHistoryUiState(items = emptyList())
            )
}