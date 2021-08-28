package study.me

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import study.me.route.configureOrderRouting

fun main() {
    embeddedServer(Netty, port = 8090, host = "localhost") {
        install(ContentNegotiation) {
            json()
        }
        install(StatusPages) {
            exception<Throwable> { cause ->
                call.respondText(cause.message!!, status = HttpStatusCode.InternalServerError)
            }
        }
        configureOrderRouting()
    }.start(wait = true)
}
