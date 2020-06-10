package io.github.multicatch.youtube

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.multicatch.papajbot.EventHandler
import io.github.multicatch.papajbot.model.*
import java.io.File

class YouTubeVideoHandler(
        private val payloadTrigger: String = "video",
        dumpFile: String = "youtube_dump.json",
        objectMapper: ObjectMapper
) : EventHandler {
    private val videos: List<String>

    init {
        val dumpStream = if (File(dumpFile).exists()) {
            File(dumpFile).inputStream()
        } else {
            YouTubeVideoHandler::class.java.classLoader.getResourceAsStream(dumpFile)
        }

        videos = objectMapper.readValue(dumpStream)
    }

    override fun invoke(event: Event): ApiCall? {
        val messaging = event.messaging.first()
        val payload = messaging.postback?.payload ?: return null

        if (payload != payloadTrigger) {
            return null
        }

        return messaging.respondWith(text = "https://youtube.com" + videos.random())
    }
}