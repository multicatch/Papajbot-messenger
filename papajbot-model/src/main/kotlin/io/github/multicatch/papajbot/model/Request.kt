package io.github.multicatch.papajbot.model

import com.fasterxml.jackson.annotation.JsonProperty

data class EventNotification(
        @JsonProperty("object")
        val type: EventType,
        val entry: List<Event> = listOf()
)

enum class EventType {
        @JsonProperty("page") PAGE
}

data class Event (
        val id: String,
        val time: Long,
        val messaging: List<MessagingItem> = listOf()
)

data class MessagingItem(
        val sender: ConversationParticipant,
        val recipient: ConversationParticipant,
        val message: Message? = null,
        val postback: Postback? = null
)

data class ConversationParticipant(
        val id: String
)

data class Message(
        @JsonProperty("is_echo")
        val isEcho: Boolean = false,
        val text: String? = null
)

data class Postback(
        val payload: String? = null
)