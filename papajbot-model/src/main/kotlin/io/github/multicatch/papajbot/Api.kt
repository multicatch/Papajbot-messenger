package io.github.multicatch.papajbot

import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.MessengerConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.ResponseException
import io.ktor.client.request.post
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.slf4j.LoggerFactory

class MessengerApi(
        private val url: String = "https://graph.facebook.com/v2.6/me/",
        private val token: String,
        private val client: HttpClient,
        private val json: Json = Json(configuration = JsonConfiguration.Default)
) {
    private val logger = LoggerFactory.getLogger(MessengerApi::class.java.name)

    fun configure(configuration: MessengerConfiguration) {
        runBlocking {
            val stringConfiguration = json.stringify(MessengerConfiguration.serializer(), configuration)
            logger.info("Sending configuration: $stringConfiguration")
            send("messenger_profile", stringConfiguration)
        }
    }

    fun send(apiCall: ApiCall) {
        runBlocking {
            send("messages", json.stringify(ApiCall.serializer(), apiCall))
        }
    }

    private suspend fun send(endpoint: String, requestBody: String) {
        val fullUrl = """$url$endpoint"""
        logger.debug("Messenger API request to $fullUrl: $requestBody")
        try {
            val response = client.post<String>("""$fullUrl?access_token=$token""") {
                contentType(ContentType.Application.Json)
                body = requestBody
            }
            logger.debug("Messenger API response: $response")
        } catch (e: ResponseException) {
            logger.warn("An error occurred while making a request to $fullUrl, details: ${e.message}, response: ${e.response.readText()}")
        }
    }
}

