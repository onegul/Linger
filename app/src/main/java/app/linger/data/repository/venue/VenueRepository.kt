package app.linger.data.repository.venue

import app.linger.core.util.Result
import app.linger.domain.model.Profile

interface VenueRepository {
    suspend fun getNearbyVenues(latitude: Double, longitude: Double, radiusMeters: Int = 100):
            Result<List<Profile>>
}