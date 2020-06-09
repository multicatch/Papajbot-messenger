package io.github.multicatch.papajbot

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event
import io.github.multicatch.papajbot.model.EventNotification
import io.github.multicatch.papajbot.model.SenderAction
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respondText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
    val eventNotification = PapajJson.readValue<EventNotification>(body)
    response.status(HttpStatusCode.OK)
    GlobalScope.launch {
            eventNotification
                    .entry
                    .filter { it.messaging.first().message?.isEcho != true }
                    .answerAll(eventHandlers, api)
    }
}

fun List<Event>.answerAll(eventHandlers: List<EventHandler>, api: MessengerApi) = forEach { it.answer(eventHandlers, api) }

fun Event.answer(eventHandlers: List<EventHandler>, api: MessengerApi) = also { event ->
    event.startReply(api)
    var result: ApiCall? = null
    var handler: EventHandler? = null

    for (currentHandler in eventHandlers) {
        handler = currentHandler
        result = currentHandler(event)
        if (result != null) {
            break
        }
    }

    if (result != null) {
        serverLogger.info("Event handled successfully, calling API.")
        val response = api.send(result)
        handler?.also {
            it.callback(result, response, PapajJson)
        }
    }
    event.stopReply(api)
}

fun Event.startReply(api: MessengerApi) {
    if (isTextMessage()) {
        return
    }

    api.send(ApiCall(
            recipient = messaging.first().sender,
            senderAction = SenderAction.MARK_SEEN
    ))

    api.send(ApiCall(
            recipient = messaging.first().sender,
            senderAction = SenderAction.TYPING_ON
    ))
}

fun Event.stopReply(api: MessengerApi) {
    if (isTextMessage()) {
        return
    }

    api.send(ApiCall(
            recipient = messaging.first().sender,
            senderAction = SenderAction.TYPING_OFF
    ))
}

fun Event.isTextMessage() =
        messaging.first().message?.isEcho == true
                && (messaging.first().message != null || messaging.first().postback != null)

val PapajJson = ObjectMapper()
        .registerModule(KotlinModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)