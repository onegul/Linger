package app.linger.data.remote.api

import app.linger.data.remote.dto.ChatMessageDto
import app.linger.data.remote.dto.ChatThreadDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("threads")
    suspend fun getThreads(): List<ChatThreadDto>

    @GET("threads/{threadId}/messages")
    suspend fun getMessages(
        @Path("threadId") threadId: String,
        @Query("since") sinceTimestamp: Long? = null
    ): List<ChatMessageDto>

    @POST("threads/{threadId}/messages")
    suspend fun sendMessage(
        @Path("threadId") threadId: String,
        @Body body: SendMessageBody
    ): ChatMessageDto

    @POST("threads")
    suspend fun createThread(@Body body: CreateThreadBody): ChatThreadDto
}

data class SendMessageBody(
    val content: String
)

data class CreateThreadBody(
    val peerId: String,
    val mode: String
)