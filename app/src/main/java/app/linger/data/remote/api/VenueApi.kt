package app.linger.data.remote.api

import app.linger.data.remote.dto.VenueDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VenueApi {
    @GET("venues/nearby")
    suspend fun getNearbyVenues(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius_m") radiusMeters: Int = 100
    ): List<VenueDto>
}