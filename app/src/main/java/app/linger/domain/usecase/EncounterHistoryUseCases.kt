package app.linger.domain.usecase

import app.linger.data.repository.encounter.EncounterRepository
import app.linger.domain.model.Encounter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEncounterHistoryUseCase @Inject constructor(
    private val encounterRepository: EncounterRepository
) {
    operator fun invoke(): Flow<List<Encounter>> = encounterRepository.observeAll()
}

class ObserveRemoteFriendsUseCase @Inject constructor(
    private val encounterRepository: EncounterRepository
) {
    operator fun invoke(): Flow<List<Encounter>> = encounterRepository.observeRemoteFriends()
}