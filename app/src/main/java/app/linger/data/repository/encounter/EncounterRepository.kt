package app.linger.data.repository.encounter

import app.linger.domain.model.Encounter
import kotlinx.coroutines.flow.Flow

interface EncounterRepository {
    fun observeAll(): Flow<List<Encounter>>

    fun observeRemoteFriends(): Flow<List<Encounter>>

    suspend fun upsertEncounters(encounters: List<Encounter>)

    suspend fun syncFromRemote()

    suspend fun markLocalChatStarted(otherId: String, started: Boolean)

    suspend fun markRemoteFriend(otherId: String, remote: Boolean)
}