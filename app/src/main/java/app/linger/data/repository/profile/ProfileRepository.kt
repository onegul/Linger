package app.linger.data.repository.profile

import app.linger.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(id: String): Flow<Profile?>

    suspend fun getProfileOnce(id: String): Profile?

    suspend fun refreshProfile(id: String)

    suspend fun upsertLocalProfile(profile: Profile)
}