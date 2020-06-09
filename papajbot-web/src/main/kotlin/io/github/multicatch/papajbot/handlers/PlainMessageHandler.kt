package io.github.multicatch.papajbot.handlers

import io.github.multicatch.papajbot.EventHandler
import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event
import io.github.multicatch.papajbot.model.ResponseMessage

class PlainMessageHandler : EventHandler {
    override fun invoke(event: Event): ApiCall? {
        val messaging = event.messaging.first()
        val message = messaging.message
        if (message != null && !message.isEcho) {
            return ApiCall(
                    recipient = event.messaging.first().sender,
                    message = ResponseMessage(
                            text = "Echo: ${message.text}"
                    )
            )
        }
        return null
    }

}