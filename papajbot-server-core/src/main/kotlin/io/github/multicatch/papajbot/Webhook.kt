package io.github.multicatch.papajbot

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event
import io.github.multicatch.papajbot.model.EventNotification
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respondText

suspend fun ApplicationCall.verifyWebhook(verifyToken: String) {
    serverLogger.info("Got webhook verification event")
    val tokenKey = "hub.verify_token"
    if (request.queryParameters[tokenKey] == verifyToken) {
        serverLogger.debug("$tokenKey is valid")
        respondText(request.queryParameters["hub.challenge"] ?: "", ContentType.Text.Plain)
    } else {
        serverLogger.debug("$tokenKey is incorrect")
        respondText("Wrong token", ContentType.Text.Plain)
    }
}

suspend fun ApplicationCall.respondToWebhookEvent(
        api: MessengerApi,
        initialHandlers: List<EventHandler> = listOf(),
        configuration: MutableList<EventHandler>.() -> Unit = {}
) {
    val eventHandlers = initialHandlers.toMutableList().apply(configuration).toList()
    serverLogger.debug("Current configuration: ${eventHandlers.size} Event Handlers")
    val body = receiveText()
    serverLogger.debug("Received an event notification: $body")
    val eventNotification = io.github.multicatch.papajbot.PapajJson.readValue<EventNotification>(body)

    val action: (Event) -> Unit = { event ->
        val result = eventHandlers.fold(null as ApiCall?) { result, handler ->
            result ?: handler(event)
        }

        if (result != null) {
            serverLogger.info("Event handled successfully, calling API.")
            api.send(result)
        }
    }
    eventNotification.entry.forEach(action)

    response.status(HttpStatusCode.OK)
}

val PapajJson = ObjectMapper()
        .registerModule(KotlinModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)