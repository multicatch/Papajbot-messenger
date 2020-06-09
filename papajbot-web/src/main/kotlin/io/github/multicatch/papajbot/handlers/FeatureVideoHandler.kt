package io.github.multicatch.papajbot.handlers

import io.github.multicatch.papajbot.EventHandler
import io.github.multicatch.papajbot.model.*

class FeatureVideoHandler : EventHandler {
    override fun invoke(event: Event): ApiCall? {
        val messaging = event.messaging.first()
        val message = messaging.message

        if (message?.text != "21:37") {
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
                                                url = "https://business.facebook.com/570013576684872/videos/570244656661764/"
                                        )
                                )
                        )
                )
        )
    }

}