package app.linger.ui.lounge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linger.core.util.Result
import app.linger.domain.model.Profile
import app.linger.domain.usecase.ScanNearbyUseCase
import app.linger.domain.usecase.StartLocalChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoungeViewModel @Inject constructor(
    scanNearbyUseCase: ScanNearbyUseCase,
    private val startLocalChatUseCase: StartLocalChatUseCase
) : ViewModel() {
    private val selfProfile = Profile(
        id = "self",
        alias = "You",
        lifeTags = listOf("books", "qawwali", "libraries", "walking"),
        currentReading = listOf("Demons"),
        pastReading = listOf("Tusculan Disputations"),
        activities = listOf("reading", "night walks"),
        music = listOf("qawwali"),
        foods = listOf("Biryani"),
        videos = listOf("TheWeasle"),
        isVenue = false
    )

    val uiState: StateFlow<LoungeUiState> =
        scanNearbyUseCase.execute(selfProfile)
            .map { encounters ->
                LoungeUiState(
                    isScanning = true,
                    items = encounters
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = LoungeUiState(isScanning = false, items = emptyList())
            )

    fun onGlance(peerId: String, onThreadReady: (String) -> Unit) {
        viewModelScope.launch {
            when (val result = startLocalChatUseCase(peerId)) {
                is Result.Success -> onThreadReady(result.value.id)
                is Result.Error,
                Result.Loading -> Unit      // TODO: error handling later
            }
        }
    }
}