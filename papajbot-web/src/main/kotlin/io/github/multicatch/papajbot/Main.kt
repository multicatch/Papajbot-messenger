package io.github.multicatch.papajbot

import io.github.multicatch.papajbot.handlers.PlainMessageHandler
import io.github.multicatch.papajbot.model.GetStartedAction
import io.github.multicatch.papajbot.model.MessengerConfiguration
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore

fun main() {
    mainLogger.info("""
░░░░░░░░░░░░░▄▄▀▀▀▀▀▀▄▄
░░░░░░░░░░▄▄▀▄▄▄█████▄▄▀▄
░░░░░░░░▄█▀▒▀▀▀█████████▄█▄
░░░░░░▄██▒▒▒▒▒▒▒▒▀▒▀▒▀▄▒▀▒▀▄
░░░░▄██▀▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▄
░░░░██▀▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▌
░░░▐██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▐█
░▄▄███▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█
▐▒▄▀██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▐▌
▌▒▒▌▒▀▒▒▒▒▒▒▄▀▀▄▄▒▒▒▒▒▒▒▒▒▒▒▒█▌
▐▒▀▒▌▒▒▒▒▒▒▒▄▄▄▄▒▒▒▒▒▒▒▀▀▀▀▄▒▐
░█▒▒▌▒▒▒▒▒▒▒▒▀▀▒▀▒▒▐▒▄▀██▀▒▒▒▌
░░█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▐▒▒▒▒▒▒▒▒█
░░░▀▌▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▌▒▒▒▒▒▒▄▀
░░░▐▒▒▒▒▒▒▒▒▒▄▀▐▒▒▒▒▒▐▒▒▒▒▄▀
░░▄▌▒▒▒▒▒▒▒▄▀▒▒▒▀▄▄▒▒▒▌▒▒▒▐▀▀▀▄▄▄
▄▀░▀▄▒▒▒▒▒▒▒▒▀▀▄▄▄▒▄▄▀▌▒▒▒▌░░░░░░
▐▌░░░▀▄▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▄▀░░░░░░░
░█░░░░░▀▄▄▒▒▒▒▒▒▒▒▒▒▒▒▄▀░█░░░░░░░
░░█░░░░░░░▀▄▄▄▒▒▒▒▒▒▄▀░░░░█░░░░░░
░░░█░░░░░░░░░▌▀▀▀▀▀▀▐░░░░░▐▌░░░░░ 
    """)

    val api = MessengerApi(
            token = System.getenv("PAPAJ_FB_TOKEN"),
            client = HttpClient(Apache)
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
        module { papajbot(verifyToken, api) }
    }

    mainLogger.info("Starting Papajbot Messenger server...")
    val server = embeddedServer(Netty, environment)
    server.start()

    mainLogger.info("Server started.")
    api.configure(MessengerConfiguration(
            getStarted = GetStartedAction(payload = "witam")
    ))
}

fun Application.papajbot(verifyToken: String, api: MessengerApi) {
    routing {
        get("/webhook/") {
            call.verifyWebhook(verifyToken)
        }
        post("/webhook/") {
            call.respondToWebhookEvent(api) {
                add(PlainMessageHandler())
            }
        }
    }
}

fun ApplicationEngineEnvironmentBuilder.ssl(
        alias: String,
        keyStorePath: String,
        keyStorePassword: String,
        privateKeyPassword: String,
        builder: EngineSSLConnectorBuilder.() -> Unit
) {
    val keyStore = KeyStore.getInstance("JKS").apply {
        load(File(keyStorePath).inputStream(), keyStorePassword.toCharArray())
    }

    sslConnector(
            keyStore = keyStore,
            keyAlias = alias,
            keyStorePassword = { keyStorePassword.toCharArray() },
            privateKeyPassword = { privateKeyPassword.toCharArray() },
            builder = builder
    )
}

val mainLogger: Logger = LoggerFactory.getLogger("io.github.multicatch.papajbot.Main")