package io.github.multicatch.videos

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.multicatch.papajbot.EventHandler
import io.github.multicatch.papajbot.model.*
import kotlin.random.Random

class VideoResponseHandler(
        private val baseVideoUrl: String,
        private val videoCache: MutableMap<String, String> = mutableMapOf(),
        private val responseProbability: Double = 1.0
) : EventHandler {

    private val openKeyWords = listOf(
            "jak", "to co", "ale co", "dlaczego", "kiedy", "o któr", "w który", "z który"
    )

    private val closedKeyWords = listOf(
            "czy", "jeśli", "gdy", "gdy", "jeżeli", "jezeli"
    )

    private val entertainedKeyWords = listOf(
            "xd", "ha", "heh", "hah", "hyh", "\uD83D\uDE02"
    )

    private val closedQuestionResponses = listOf(
            "niewiem.mp4",
            "mozna.mp4",
            "co.mp4",
            "jestmozliwe.mp4",
            "jeszczejak.mp4"
    )

    private val openQuestionResponses = listOf(
            "niewiem.mp4",
            "niemoge.mp4",
            "co.mp4",
            "poco.mp4"
    )

    private val entertainedResponse = "hyhyhy.mp4"

    override fun invoke(event: Event): ApiCall? {
        val messaging = event.messaging.first()
        val message = messaging.message?.text ?: return null

        val reactionVideo = message.reactionVideo()
        val random = Random.nextDouble(0.0, 1.0)
        if (reactionVideo != null && random < responseProbability) {
            val video = "$baseVideoUrl/$reactionVideo"
            val cachedId = videoCache[video]
            val payload = if (cachedId == null) {
                AttachmentPayload(
                        url = video,
                        isReusable = true
                )
            } else {
                AttachmentPayload(
                        id = cachedId
                )
            }

            return messaging.respondWith(
                    attachment = Attachment(
                            type = AttachmentType.VIDEO,
                            payload = payload
                    )
            )
        }

        return null
    }

    override fun callback(request: ApiCall, response: String?, objectMapper: ObjectMapper) {
        if (response != null) {
            val responseTree = objectMapper.readTree(response)
            val attachmentIdNode: JsonNode? = responseTree["attachment_id"]
            if (attachmentIdNode != null) {
                val video = request.message?.attachment?.payload?.url ?: return
                val attachmentId = attachmentIdNode.textValue()
                videoCache[video] = attachmentId
            }
        }
    }

    private fun String.reactionVideo(): String? {
        val lowerCaseMessage = toLowerCase()

        if (closedKeyWords.any { lowerCaseMessage.startsWith(it) }) {
            return closedQuestionResponses.random()
        }

        if (openKeyWords.any { lowerCaseMessage.contains(it) } || lowerCaseMessage.contains("?")) {
            return openQuestionResponses.random()
        }

        if (entertainedKeyWords.any { lowerCaseMessage.contains(it) }) {
            return entertainedResponse
        }

        return null
    }
}