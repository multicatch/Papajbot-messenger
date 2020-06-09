package io.github.multicatch.papajbot

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.MessengerConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.post
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class MessengerApi(
        private val url: String = "https://graph.facebook.com/v2.6/me/",
        private val token: String,
        private val client: HttpClient,
        private val json: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(MessengerApi::class.java.name)

    fun configure(configuration: MessengerConfiguration): String? {
        val requestBody = json.writeValueAsString(configuration)
        return runBlocking {
            logger.info("Sending configuration: $requestBody")
            send("messenger_profile", requestBody)
        }
    }

    fun send(apiCall: ApiCall): String? {
        val requestBody = json.writeValueAsString(apiCall)
        return runBlocking {
            send("messages", requestBody)
        }
    }

    private suspend fun send(endpoint: String, requestBody: String): String? {
        val fullUrl = """$url$endpoint"""
        logger.debug("Messenger API request to $fullUrl: $requestBody")
        try {
            val response = client.post<String>("""$fullUrl?access_token=$token""") {
                contentType(ContentType.Application.Json)
                body = requestBody
            }
            logger.debug("Messenger API response: $response")
            return response
        } catch (e: ResponseException) {
            logger.warn("An error occurred while making a request to $fullUrl, details: ${e.message}, response: ${e.response.readText()}")
        }
        return null
    }
}

