package study.me

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import study.me.route.configureOrderRouting

fun main() {
    embeddedServer(Netty, port = 8090, host = "localhost") {
        install(ContentNegotiation) {
            json()
        }
        configureOrderRouting()
    }.start(wait = true)
}
