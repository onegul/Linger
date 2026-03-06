package app.linger.data.remote.api

import app.linger.data.remote.dto.EncounterDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryApi {
    @GET("encounters")
    suspend fun getEncounters(
        @Query("since") sinceTimestamp: Long? = null,
        @Query("limit") limit: Int = 100
    ): List<EncounterDto>
}