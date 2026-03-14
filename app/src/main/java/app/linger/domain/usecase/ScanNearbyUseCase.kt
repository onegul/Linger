package app.linger.domain.usecase

import app.linger.core.util.DispatcherProvider
import app.linger.data.repository.encounter.EncounterRepository
import app.linger.data.repository.profile.ProfileRepository
import app.linger.data.repository.venue.VenueRepository
import app.linger.domain.model.Encounter
import app.linger.domain.model.EncounterType
import app.linger.domain.model.Profile
import app.linger.ml.ResonanceEngine
import app.linger.proximity.ProximityScanner
import app.linger.proximity.model.ProximityTargetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScanNearbyUseCase @Inject constructor(
    private val proximityScanner: ProximityScanner,
    private val profileRepository: ProfileRepository,
    private val venueRepository: VenueRepository,
    private val resonanceEngine: ResonanceEngine,
    private val dispatchers: DispatcherProvider
) {
    @Inject
    lateinit var encounterRepository: EncounterRepository

    /**
     * Start scanning; returns a flow of current encounters with resonance.
     * For now, venues are fetched synchronously via VenueRepository each time this is called.
     */
    fun execute(selfProfile: Profile): Flow<List<Encounter>> {
        proximityScanner.startScanning()

        return proximityScanner.proximityHits.map { hits ->
            hits.mapNotNull { hit ->
                val type = when (hit.type) {
                    ProximityTargetType.USER -> EncounterType.USER
                    ProximityTargetType.VENUE -> EncounterType.VENUE
                }

                // For now we only handle USER resonance locally;
                // venues can be given a neutral resonance or computed similarly.
                val otherProfile: Profile? =
                    null    // placeholder; will be resolved in a higher layer

                val resonance = otherProfile?.let { other ->
                    resonanceEngine.computeResonance(selfProfile, other)
                }

                Encounter(
                    id = hit.targetId + "_" + hit.firstSeenAt.toEpochMilliseconds(),
                    otherId = hit.targetId,
                    type = type,
                    firstSeenAt = hit.firstSeenAt,
                    lastSeenAt = hit.lastSeenAt,
                    approximateDistanceMeters = hit.distanceMeters,
                    resonance = resonance,
                    wasLocalChatStarted = false,
                    isRemoteFriend = false
                )
            }
        }
    }

    suspend fun syncRemoteHistory() {
        withContext(dispatchers.io) {
            (/* injected */ encounterRepository).syncFromRemote()
        }
    }
}