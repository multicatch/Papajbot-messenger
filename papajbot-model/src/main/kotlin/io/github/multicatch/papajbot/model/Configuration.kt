package io.github.multicatch.papajbot.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MessengerConfiguration(
        @JsonProperty("get_started")
        val getStarted: GetStartedAction? = null
)

data class GetStartedAction(
        val payload: String
)