package app.linger.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linger.core.util.Result
import app.linger.domain.model.ChatMode
import app.linger.domain.usecase.KeepInTouchUseCase
import app.linger.domain.usecase.ObserveChatMessagesUseCase
import app.linger.domain.usecase.ObserveChatThreadsUseCase
import app.linger.domain.usecase.RefreshChatMessagesUseCase
import app.linger.domain.usecase.SendMessagesUseCase
import app.linger.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeChatMessages: ObserveChatMessagesUseCase,
    observeChatThreads: ObserveChatThreadsUseCase,
    private val refreshChatMessages: RefreshChatMessagesUseCase,
    private val sendMessage: SendMessagesUseCase,
    private val keepInTouchUseCase: KeepInTouchUseCase
) : ViewModel() {
    private val threadId: String = checkNotNull(savedStateHandle[Routes.CHAT_THREAD_ID])

    private val messagesFlow = observeChatMessages(threadId)
    private val threadsFlow = observeChatThreads()

    val uiState: StateFlow<ChatUiState> =
        combine(messagesFlow, threadsFlow) { messages, threads ->
            val thread = threads.firstOrNull { it.id == threadId }
            ChatUiState(
                threadId = threadId,
                mode = thread?.mode ?: ChatMode.LOCAL,
                title = thread?.peerId ?: "Chat",
                messages = messages,
                sending = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChatUiState(
                threadId = threadId,
                mode = ChatMode.LOCAL,
                title = "Chat",
                messages = emptyList(),
                sending = false
            )
        )

    fun refresh() {
        viewModelScope.launch { refreshChatMessages(threadId) }
    }

    fun send(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            when (sendMessage(threadId, text.trim())) {
                is Result.Success -> Unit
                is Result.Error -> Unit     // TODO: expose snackbar or toast
                Result.Loading -> Unit
            }
        }
    }

    fun keepInTouch() {
        viewModelScope.launch {
            when (keepInTouchUseCase(threadId)) {
                is Result.Success -> Unit
                is Result.Error -> Unit     // TODO: expose snackbar or toast later
                Result.Loading -> Unit
            }
        }
    }
}