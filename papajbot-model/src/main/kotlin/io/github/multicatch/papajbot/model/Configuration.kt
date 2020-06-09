package io.github.multicatch.papajbot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessengerConfiguration(
        @SerialName("get_started")
        val getStarted: GetStartedAction? = null
)

@Serializable
data class GetStartedAction(
        val payload: String
)