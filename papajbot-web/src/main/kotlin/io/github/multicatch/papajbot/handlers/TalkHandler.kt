package io.github.multicatch.papajbot.handlers

import io.github.multicatch.papajbot.EventHandler
import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event
import io.github.multicatch.papajbot.model.respondWith
import io.github.multicatch.papajbot.talk.Responder
import io.github.multicatch.papajbot.talk.talkMoodOf
import io.github.multicatch.papajbot.talk.talkOf
import io.github.multicatch.papajbot.talk.talkTopicOf

class TalkHandler(
        val responder: Responder
) : EventHandler {
    override fun invoke(event: Event): ApiCall? {
        val messaging = event.messaging.first()
        val message = messaging.message

        if (message?.text == null) {
            return null
        }

        val text = message.text ?: ""
        val talk = talkOf(talkTopicOf("default", talkMoodOf(0f)))

        val (response, _) = responder.respond(text, talk)

        return messaging.respondWith(response)
    }

}