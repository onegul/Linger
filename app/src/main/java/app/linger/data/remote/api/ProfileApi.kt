package app.linger.data.remote.api

import app.linger.data.remote.dto.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileApi {
    @GET("me")
    suspend fun getMyProfile(): UserProfileDto

    @GET("profiles/{id}")
    suspend fun getProfile(@Path("id") id: String): UserProfileDto

    @POST("profiles/resolve-proximity-id")
    suspend fun resolveProximityId(@Body body: ResolveProximityIdBody): ResolveProximityIdResponse
}

data class ResolveProximityIdBody(
    val scannedId: String
)

data class ResolveProximityIdResponse(
    val canonicalId: String
)