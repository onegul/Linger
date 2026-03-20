package app.linger.ui.lounge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linger.core.util.Result
import app.linger.data.repository.profile.ProfileRepository
import app.linger.domain.model.Encounter
import app.linger.domain.model.EncounterType
import app.linger.domain.model.Profile
import app.linger.domain.model.ResonanceLevel
import app.linger.domain.usecase.ScanNearbyUseCase
import app.linger.domain.usecase.StartLocalChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoungeViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var scanNearbyUseCase: ScanNearbyUseCase

    @Inject
    lateinit var startLocalChatUseCase: StartLocalChatUseCase

    @Inject
    lateinit var profileRepository: ProfileRepository

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = scanNearbyUseCase.execute(selfProfile)
        .transformLatest { encounters ->
            val items = withContext(Dispatchers.IO) {
                encounters.map { encounter -> toLoungeItem(encounter) }
            }
            emit(LoungeUiState(isScanning = true, items = items))
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
                is Result.Error, Result.Loading -> Unit      // TODO: error handling later
            }
        }
    }

    private suspend fun toLoungeItem(encounter: Encounter): LoungeItem {
        val resonanceLevel: ResonanceLevel? = encounter.resonance?.level

        val profile: Profile? = when (encounter.type) {
            EncounterType.USER, EncounterType.VENUE -> profileRepository.getProfileOnce(encounter.otherId)
        }

        return when (encounter.type) {
            EncounterType.USER -> {
                val reading =
                    profile?.currentReading?.firstOrNull() ?: profile?.pastReading?.firstOrNull()
                val tags = (profile?.lifeTags ?: emptyList()).take(3)
                val activity = profile?.activities?.firstOrNull() ?: profile?.music?.firstOrNull()

                val primary = when {
                    reading != null -> "Reading: $reading"
                    activity != null -> "Active: $activity"
                    else -> "Nearby profile"
                }

                val secondary = listOf(
                    tags.joinToString(" * ").ifEmpty { "" },
                    activity?.let { " · $it" }.orEmpty()
                ).joinToString("").trim()

                LoungeItem(
                    encounterId = encounter.id,
                    peerId = encounter.otherId,
                    type = encounter.type,
                    resonanceLevel = resonanceLevel,
                    distanceMeters = encounter.approximateDistanceMeters,
                    essencePrimary = primary,
                    essenceSecondary = secondary.ifEmpty { "Essence nearby" }
                )
            }

            EncounterType.VENUE -> {
                val tags = (profile?.lifeTags ?: emptyList()).take(3)

                LoungeItem(
                    encounterId = encounter.id,
                    peerId = encounter.otherId,
                    type = encounter.type,
                    resonanceLevel = null,
                    distanceMeters = encounter.approximateDistanceMeters,
                    essencePrimary = "Echo nearby",
                    essenceSecondary = tags.joinToString(" * ").ifEmpty { "Local atmosphere" }
                )
            }
        }
    }
}