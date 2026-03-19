package app.linger.domain.usecase

import app.linger.core.util.Result
import app.linger.data.repository.chat.ChatRepository
import app.linger.data.repository.encounter.EncounterRepository
import app.linger.domain.model.ChatMode
import app.linger.domain.model.ChatThread
import javax.inject.Inject

class StartLocalChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val encounterRepository: EncounterRepository
) {
    suspend operator fun invoke(peerId: String): Result<ChatThread> {
        val result = chatRepository.createThread(peerId = peerId, mode = ChatMode.LOCAL)
        return when (result) {
            is Result.Success -> {
                encounterRepository.markLocalChatStarted(peerId, true)
                encounterRepository.markRemoteFriend(peerId, false)
                result
            }

            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}