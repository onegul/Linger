package app.linger.data.mapper

import app.linger.data.local.entity.ChatMessageEntity
import app.linger.data.local.entity.ChatThreadEntity
import app.linger.data.remote.dto.ChatMessageDto
import app.linger.data.remote.dto.ChatThreadDto
import app.linger.domain.model.ChatMessage
import app.linger.domain.model.ChatMode
import app.linger.domain.model.ChatThread
import app.linger.domain.model.MessageSender
import app.linger.domain.model.MessageStatus

fun ChatThreadEntity.toDomain(): ChatThread =
    ChatThread(
        id = id,
        peerId = peerId,
        mode = mode,
        lastMessagePreview = lastMessagePreview,
        lastUpdatedAtMillis = lastUpdatedAtMillis,
        isMuted = isMuted
    )

fun ChatThread.toEntity(): ChatThreadEntity =
    ChatThreadEntity(
        id = id,
        peerId = peerId,
        mode = mode,
        lastMessagePreview = lastMessagePreview,
        lastUpdatedAtMillis = lastUpdatedAtMillis,
        isMuted = isMuted
    )

fun ChatThreadDto.toEntity(): ChatThreadEntity =
    ChatThreadEntity(
        id = id,
        peerId = peerId,
        mode = when (mode.uppercase()) {
            "REMOTE" -> ChatMode.REMOTE
            else -> ChatMode.LOCAL
        },
        lastMessagePreview = lastMessagePreview,
        lastUpdatedAtMillis = lastUpdatedAt,
        isMuted = isMuted
    )

fun ChatMessageEntity.toDomain(): ChatMessage =
    ChatMessage(
        id = id,
        threadId = threadId,
        sender = sender,
        content = content,
        timestampMillis = timestampMillis,
        status = status
    )

fun ChatMessage.toEntity(): ChatMessageEntity =
    ChatMessageEntity(
        id = id,
        threadId = threadId,
        sender = sender,
        content = content,
        timestampMillis = timestampMillis,
        status = status
    )

fun ChatMessageDto.toEntity(): ChatMessageEntity =
    ChatMessageEntity(
        id = id,
        threadId = threadId,
        sender = when (sender.uppercase()) {
            "OTHER" -> MessageSender.OTHER
            else -> MessageSender.SELF
        },
        content = content,
        timestampMillis = timestamp,
        status = when (status.uppercase()) {
            "DELIVERED" -> MessageStatus.DELIVERED
            "FAILED" -> MessageStatus.FAILED
            "SENT" -> MessageStatus.SENT
            else -> MessageStatus.PENDING
        }
    )