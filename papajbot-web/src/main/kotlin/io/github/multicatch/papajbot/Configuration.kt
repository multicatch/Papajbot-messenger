package io.github.multicatch.papajbot

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.EngineSSLConnectorBuilder
import io.ktor.server.engine.sslConnector
import java.io.File
import java.security.KeyStore

fun Application.papajbot(verifyToken: String, api: MessengerApi, handlers: List<EventHandler>) {
    routing {
        get("/webhook/") {
            call.verifyWebhook(verifyToken)
        }
        post("/webhook/") {
            call.respondToWebhookEvent(api, handlers)
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