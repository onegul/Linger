package app.linger.data.repository.venue

import app.linger.core.util.DispatcherProvider
import app.linger.core.util.Result
import app.linger.data.remote.api.VenueApi
import app.linger.domain.model.Profile
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VenueRepositoryImpl @Inject constructor(
    private val venueApi: VenueApi,
    private val dispatchers: DispatcherProvider
) : VenueRepository {
    override suspend fun getNearbyVenues(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): Result<List<Profile>> = withContext(dispatchers.io) {
        try {
            val dtos = venueApi.getNearbyVenues(latitude, longitude, radiusMeters)
            val profiles = dtos.map { dto ->
                Profile(
                    id = dto.id,
                    alias = dto.name,
                    lifeTags = dto.tags ?: emptyList(),
                    currentReading = emptyList(),
                    pastReading = emptyList(),
                    activities = emptyList(),
                    music = emptyList(),
                    foods = emptyList(),
                    videos = emptyList(),
                    isVenue = true
                )
            }
            Result.Success(profiles)
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }
}