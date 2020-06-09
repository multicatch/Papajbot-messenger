package io.github.multicatch.papajbot

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.EngineSSLConnectorBuilder
import io.ktor.server.engine.sslConnector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore
import java.util.*

const val CONFIG_START = "Cl9fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX19fX18KICAgIF9fX18gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgLyAgICApICAgICAgICAgICAgICAgICAgICAgICAgICAgLCAgIC8gICAgICAgICAgICAgCi0tLS9fX19fLy0tLS1fXy0tLS0tLV9fLS0tLV9fLS0tLS0tLS0tLS0vX18tLS0tX18tLV8vXy0KICAvICAgICAgICAvICAgKSAgIC8gICApIC8gICApICAgICAvICAgLyAgICkgLyAgICkgLyAgIApfL19fX19fX19fKF9fXyhfX18vX19fL18oX19fKF9fX19fL19fXyhfX18vXyhfX18vXyhfIF9fCiAgICAgICAgICAgICAgICAgLyAgICAgICAgICAgICAgIC8gICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgIC8gICAgICAgICAgICAoXyAvICAgICAgICAgICAgICAgICAgICAgIAo="

fun Application.papajbot(verifyToken: String, api: MessengerApi, handlers: List<EventHandler>) {
    serverLogger.info(String(Base64.getDecoder().decode(CONFIG_START)))
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

val serverLogger: Logger = LoggerFactory.getLogger("io.github.multicatch.papajbot.Configuration")