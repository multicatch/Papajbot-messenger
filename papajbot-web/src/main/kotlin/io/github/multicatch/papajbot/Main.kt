package io.github.multicatch.papajbot

import io.github.multicatch.papajbot.handlers.FeatureVideoHandler
import io.github.multicatch.papajbot.handlers.PlainMessageHandler
import io.github.multicatch.papajbot.handlers.TalkHandler
import io.github.multicatch.papajbot.model.GetStartedAction
import io.github.multicatch.papajbot.model.MessengerConfiguration
import io.github.multicatch.papajbot.talk.*
import io.github.multicatch.videos.VideoResponseHandler
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.HttpTimeout
import io.ktor.http.content.files
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore
import java.util.*

const val APPLICATION_HEARTBEAT = "CuKWkeKWkeKWkeKWkeKWkeKWkeKWkeKWkeKWkeKWkeKWkeKWkeKWkeKWhOKWhOKWgOKWgOKWgOKWgOKWgOKWgOKWhOKWhArilpHilpHilpHilpHilpHilpHilpHilpHilpHilpHiloTiloTiloDiloTiloTiloTilojilojilojilojilojiloTiloTiloDiloQK4paR4paR4paR4paR4paR4paR4paR4paR4paE4paI4paA4paS4paA4paA4paA4paI4paI4paI4paI4paI4paI4paI4paI4paI4paE4paI4paECuKWkeKWkeKWkeKWkeKWkeKWkeKWhOKWiOKWiOKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWgOKWkuKWgOKWkuKWgOKWhOKWkuKWgOKWkuKWgOKWhArilpHilpHilpHilpHiloTilojilojiloDilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilojiloQK4paR4paR4paR4paR4paI4paI4paA4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paI4paMCuKWkeKWkeKWkeKWkOKWiOKWiOKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkOKWiArilpHiloTiloTilojilojilojilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilogK4paQ4paS4paE4paA4paI4paI4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paQ4paMCuKWjOKWkuKWkuKWjOKWkuKWgOKWkuKWkuKWkuKWkuKWkuKWkuKWhOKWgOKWgOKWhOKWhOKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWiOKWjArilpDilpLiloDilpLilozilpLilpLilpLilpLilpLilpLilpLiloTiloTiloTiloTilpLilpLilpLilpLilpLilpLilpLiloDiloDiloDiloDiloTilpLilpAK4paR4paI4paS4paS4paM4paS4paS4paS4paS4paS4paS4paS4paS4paA4paA4paS4paA4paS4paS4paQ4paS4paE4paA4paI4paI4paA4paS4paS4paS4paMCuKWkeKWkeKWiOKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkOKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWiArilpHilpHilpHiloDilozilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilpLilozilpLilpLilpLilpLilpLilpLiloTiloAK4paR4paR4paR4paQ4paS4paS4paS4paS4paS4paS4paS4paS4paS4paE4paA4paQ4paS4paS4paS4paS4paS4paQ4paS4paS4paS4paS4paE4paACuKWkeKWkeKWhOKWjOKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWhOKWgOKWkuKWkuKWkuKWgOKWhOKWhOKWkuKWkuKWkuKWjOKWkuKWkuKWkuKWkOKWgOKWgOKWgOKWhOKWhOKWhAriloTiloDilpHiloDiloTilpLilpLilpLilpLilpLilpLilpLilpLiloDiloDiloTiloTiloTilpLiloTiloTiloDilozilpLilpLilpLilozilpHilpHilpHilpHilpHilpEK4paQ4paM4paR4paR4paR4paA4paE4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paS4paE4paA4paR4paR4paR4paR4paR4paR4paRCuKWkeKWiOKWkeKWkeKWkeKWkeKWkeKWgOKWhOKWhOKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWkuKWhOKWgOKWkeKWiOKWkeKWkeKWkeKWkeKWkeKWkeKWkQrilpHilpHilojilpHilpHilpHilpHilpHilpHilpHiloDiloTiloTiloTilpLilpLilpLilpLilpLilpLiloTiloDilpHilpHilpHilpHilojilpHilpHilpHilpHilpHilpEK4paR4paR4paR4paI4paR4paR4paR4paR4paR4paR4paR4paR4paR4paM4paA4paA4paA4paA4paA4paA4paQ4paR4paR4paR4paR4paR4paQ4paM4paR4paR4paR4paR4paRIAogICAg"

fun main() {
    val initialData = Base64.getDecoder().decode(APPLICATION_HEARTBEAT)
    mainLogger.info(String(initialData))

    val api = MessengerApi(
            token = System.getenv("PAPAJ_FB_TOKEN"),
            client = HttpClient(Apache) {
                install(HttpTimeout) {
                    requestTimeoutMillis = 70000
                    socketTimeoutMillis = 70000
                }
            },
            json = PapajJson
    )

    val verifyToken = System.getenv("PAPAJ_WEBHOOK_TOKEN")

    val environment = applicationEngineEnvironment {
        ssl(
                System.getenv("PAPAJ_KEYSTORE_ALIAS"),
                System.getenv("PAPAJ_KEYSTORE_LOCATION"),
                System.getenv("PAPAJ_KEYSTORE_PASS"),
                System.getenv("PAPAJ_PRIVATEKEY_PASS")
        ) {
            host = "0.0.0.0"
            port = 2137
        }
        module {
            papajbot(verifyToken, api, handlers) {
                static("static") {
                    resources("videos")
                }
            }
        }
    }

    mainLogger.info("Starting Papajbot Messenger server...")
    val server = embeddedServer(Netty, environment)
    server.start()

    mainLogger.info("Server started.")
    api.configure(MessengerConfiguration(
            getStarted = GetStartedAction(payload = "witam")
    ))
}

val mainLogger: Logger = LoggerFactory.getLogger("io.github.multicatch.papajbot.Main")

val handlers = listOf(
        FeatureVideoHandler(),
        VideoResponseHandler(
                "https://ca.vpcloud.eu:2137/static",
                responseProbability = 0.8
        ),
        TalkHandler(SimpleResponder(
                responseStrategyAdapters = listOf(
                        Greetings(),
                        Listening(),
                        Confirmation(),
                        TopicChange(),
                        OpenQuestions(),
                        ConversationContinuation()
                ),
                defaultResponses = defaultResponses
        ))
)