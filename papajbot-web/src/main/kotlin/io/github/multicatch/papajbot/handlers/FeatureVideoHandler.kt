package io.github.multicatch.papajbot.handlers

import io.github.multicatch.papajbot.EventHandler
import io.github.multicatch.papajbot.model.*

class FeatureVideoHandler(
        private val triggers: List<String> = listOf("21:37"),
        private val videoUrl: String = "https://business.facebook.com/570013576684872/videos/570244656661764/"
) : EventHandler {
    override fun invoke(event: Event): ApiCall? {
        val messaging = event.messaging.first()
        val message = messaging.message

        if (!triggers.contains(message?.text)) {
            return null
        }

        return messaging.respondWith(
                attachment = Attachment(
                        type = AttachmentType.TEMPLATE,
                        payload = AttachmentPayload(
                                templateType = TemplateType.MEDIA,
                                elements = listOf(
                                        PayloadElement(
                                                mediaType = PayloadElementType.VIDEO,
                                                url = videoUrl
                                        )
                                )
                        )
                )
        )
    }

}