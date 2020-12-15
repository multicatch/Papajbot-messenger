package io.github.multicatch.papajbot.handlers

import io.github.multicatch.papajbot.EventHandler
import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event
import io.github.multicatch.papajbot.model.respondWith


class AskQuestionPayloadHandler(
        private val trigger: String = "pytaj"
) : EventHandler {
    override fun invoke(event: Event): ApiCall? {
        val messaging = event.messaging.first()
        val payload = messaging.postback?.payload ?: return null

        if (payload != trigger) {
            return null
        }

        return messaging.respondWith("No to pytaj")
    }

}