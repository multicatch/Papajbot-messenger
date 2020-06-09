package io.github.multicatch.papajbot

import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event
import io.github.multicatch.papajbot.model.EventNotification
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respondText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

suspend fun ApplicationCall.verifyWebhook(verifyToken: String) {
    mainLogger.info("Got webhook verification event")
    val tokenKey = "hub.verify_token"
    if (request.queryParameters[tokenKey] == verifyToken) {
        mainLogger.debug("$tokenKey is valid")
        respondText(request.queryParameters["hub.challenge"] ?: "", ContentType.Text.Plain)
    } else {
        mainLogger.debug("$tokenKey is incorrect")
        respondText("Wrong token", ContentType.Text.Plain)
    }
}

suspend fun ApplicationCall.respondToWebhookEvent(
        api: MessengerApi,
        initialHandlers: List<EventHandler> = listOf(),
        configuration: MutableList<EventHandler>.() -> Unit = {}
) {
    val eventHandlers = initialHandlers.toMutableList().apply(configuration).toList()
    mainLogger.debug("Current configuration: ${eventHandlers.size} Event Handlers")
    val body = receiveText()
    mainLogger.debug("Received an event notification: $body")
    val eventNotification = PapajJson.parse(EventNotification.serializer(), body)

    val action: (Event) -> Unit = { event ->
        val result = eventHandlers.fold(null as ApiCall?) { result, handler ->
            result ?: handler(event)
        }

        if (result != null) {
            mainLogger.info("Event handled successfully, calling API.")
            api.send(result)
        }
    }
    eventNotification.entry.forEach(action)

    response.status(HttpStatusCode.OK)
}

val PapajJson = Json(configuration = JsonConfiguration(ignoreUnknownKeys = true))