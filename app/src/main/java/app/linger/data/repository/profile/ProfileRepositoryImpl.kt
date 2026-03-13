package app.linger.data.repository.profile

import app.linger.core.util.DispatcherProvider
import app.linger.data.local.dao.UserProfileDao
import app.linger.data.mapper.toDomain
import app.linger.data.mapper.toEntity
import app.linger.data.remote.api.ProfileApi
import app.linger.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val profileApi: ProfileApi,
    private val dispatchers: DispatcherProvider
) : ProfileRepository{
    override fun observeProfile(id: String): Flow<Profile?> =
        userProfileDao.observeById(id).map { it?.toDomain() }

    override suspend fun getProfileOnce(id: String): Profile? =
        withContext(dispatchers.io) {
            userProfileDao.getById(id)?.toDomain()
        }

    override suspend fun refreshProfile(id: String) {
        withContext(dispatchers.io) {
            val dto = profileApi.getProfile(id)
            val domain = dto.toDomain()
            userProfileDao.upsert(domain.toEntity())
        }
    }

    override suspend fun upsertLocalProfile(profile: Profile) {
        withContext(dispatchers.io) {
            userProfileDao.upsert(profile.toEntity())
        }
    }
}