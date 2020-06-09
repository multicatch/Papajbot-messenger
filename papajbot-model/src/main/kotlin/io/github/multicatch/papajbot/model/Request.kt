package io.github.multicatch.papajbot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventNotification(
        @SerialName("object")
        val type: EventType,
        val entry: List<Event> = listOf()
)

@Serializable
enum class EventType {
        @SerialName("page") PAGE
}

@Serializable
data class Event (
        val id: String,
        val time: Long,
        val messaging: List<MessagingItem> = listOf()
)

@Serializable
data class MessagingItem(
        val sender: ConversationParticipant,
        val recipient: ConversationParticipant,
        val message: Message? = null,
        val postback: Postback? = null
)

@Serializable
data class ConversationParticipant(
        val id: String
)

@Serializable
data class Message(
        @SerialName("is_echo")
        val isEcho: Boolean = false,
        val text: String? = null
)

@Serializable
data class Postback(
        val payload: String? = null
)