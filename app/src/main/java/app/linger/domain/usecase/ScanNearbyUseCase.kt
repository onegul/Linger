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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Instant

class ScanNearbyUseCase @Inject constructor(
    private val proximityScanner: ProximityScanner,
    private val profileRepository: ProfileRepository,
    private val venueRepository: VenueRepository,
    private val resonanceEngine: ResonanceEngine,
    private val dispatchers: DispatcherProvider,
    private val encounterRepository: EncounterRepository
) {
    /**
     * Start scanning; returns a flow of current encounters with resonance.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(selfProfile: Profile): Flow<List<Encounter>> {
        proximityScanner.startScanning()

        return proximityScanner.proximityHits.transformLatest { hits ->
            // For now, we assume targetId is a stable peerId (no ephemeral mapping yet).
            val encounters = withContext(dispatchers.io) {
                hits.map { hit ->
                    val type = when (hit.type) {
                        ProximityTargetType.USER -> EncounterType.USER
                        ProximityTargetType.VENUE -> EncounterType.VENUE
                    }

                    val resonance = if (type == EncounterType.USER) {
                        val otherProfile: Profile? = profileRepository.getProfileOnce(hit.targetId)
                        otherProfile?.let { other ->
                            resonanceEngine.computeResonance(selfProfile, other)
                        }
                    } else
                        null

                    Encounter(
                        id = buildEncounterId(hit.targetId, hit.firstSeenAt),
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

            if (encounters.isNotEmpty())
                withContext(dispatchers.io) {
                    encounterRepository.upsertEncounters(encounters)
                }

            emit(encounters)
        }
    }

    suspend fun syncRemoteHistory() {
        withContext(dispatchers.io) {
            encounterRepository.syncFromRemote()
        }
    }

    private fun buildEncounterId(otherId: String, firstSeenAt: Instant): String =
        "${otherId}_${firstSeenAt.toEpochMilliseconds()}"
}