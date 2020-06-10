package io.github.multicatch.papajbot.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MessengerConfiguration(
        @JsonProperty("get_started")
        val getStarted: GetStartedAction? = null,
        @JsonProperty("persistent_menu")
        val persistentMenu: List<PersistentMenu>? = null
)

data class GetStartedAction(
        val payload: String
)

data class PersistentMenu(
        val locale: String = "default",
        @JsonProperty("composer_input_disabled")
        val composerInputDisabled: Boolean = false,
        @JsonProperty("call_to_actions")
        val callToActions: List<ActionCall>? = null
)

data class ActionCall(
        val title: String,
        val type: ActionType,
        val payload: String? = null,
        @JsonProperty("call_to_actions")
        val callToActions: List<ActionCall>? = null
)

enum class ActionType {
        @JsonProperty("nested") NESTED,
        @JsonProperty("postback") POSTBACK
}