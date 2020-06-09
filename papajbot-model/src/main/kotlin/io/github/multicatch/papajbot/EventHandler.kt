package io.github.multicatch.papajbot

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event

interface EventHandler : (Event) -> ApiCall? {
    fun callback(request: ApiCall, response: String?, objectMapper: ObjectMapper) {
        // do nothing by default
    }
}