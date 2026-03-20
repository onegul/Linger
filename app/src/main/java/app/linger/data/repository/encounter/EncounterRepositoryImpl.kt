package app.linger.data.repository.encounter

import app.linger.core.util.DispatcherProvider
import app.linger.data.local.dao.EncounterDao
import app.linger.data.mapper.toDomain
import app.linger.data.mapper.toEntity
import app.linger.data.remote.api.HistoryApi
import app.linger.domain.model.Encounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EncounterRepositoryImpl @Inject constructor(
    private val encounterDao: EncounterDao,
    private val historyApi: HistoryApi,
    private val dispatchers: DispatcherProvider
) : EncounterRepository {
    override fun observeAll(): Flow<List<Encounter>> =
        encounterDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeRemoteFriends(): Flow<List<Encounter>> =
        encounterDao.observeRemoteFriends().map { list -> list.map { it.toDomain() } }

    override suspend fun upsertEncounters(encounters: List<Encounter>) {
        withContext(dispatchers.io) {
            if (encounters.isEmpty()) return@withContext

            val otherIds = encounters.map { it.otherId }.distinct()

            // Capture relationship state before inserting new encounter rows
            val localChatStartedById = otherIds.associateWith { id ->
                encounterDao.hasLocalChatStarted(id)
            }
            val remoteFriendById = otherIds.associateWith { id ->
                encounterDao.isRemoteFriend(id)
            }

            // Insert/update incoming encounters
            encounterDao.upsertAll(encounters.map { it.toEntity() })

            // Re-apply any previously true relationship flags
            localChatStartedById.forEach { (otherId, started) ->
                if (started) encounterDao.setWasLocalChatStarted(otherId, true)
            }
            remoteFriendById.forEach { (otherId, remote) ->
                if (remote) encounterDao.setIsRemoteFriend(otherId, true)
            }
        }
    }

    override suspend fun syncFromRemote() {
        withContext(dispatchers.io) {
            val dtos = historyApi.getEncounters()
            val entities = dtos.map { it.toEntity() }
            encounterDao.upsertAll(entities)
        }
    }

    override suspend fun markLocalChatStarted(otherId: String, started: Boolean) {
        withContext(dispatchers.io) {
            encounterDao.setWasLocalChatStarted(otherId, started)
        }
    }

    override suspend fun markRemoteFriend(otherId: String, remote: Boolean) {
        withContext(dispatchers.io) {
            encounterDao.setIsRemoteFriend(otherId, remote)
        }
    }
}