package study.me.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*
import study.me.db.DatabaseConfig
import study.me.dto.request.UpdateOrderRequest
import study.me.service.OrderService

val dbConfig = DatabaseConfig()
val orderService = OrderService(dbConfig.initDb())

fun Application.configureOrderRouting() {
    routing {
        route("/orders") {

            get {
                val orders = orderService.getAllOrders()
                call.respond(orders)
            }

            route("/{id}") {

                get {
                    val id: Long = call.parameters["id"]?.toLong() ?: return@get call.respondText(
                        "Bad Request",
                        status = HttpStatusCode.BadRequest
                    )
                    val order = orderService.getOrderById(id)
                    if (order.id == -1L) {
                        return@get call.respondText(
                            "No such order with id $id",
                            status = HttpStatusCode.BadRequest
                        )
                    }
                    call.respond(order)
                }

                put {
                    val id: Long = call.parameters["id"]?.toLong() ?: return@put call.respondText(
                        "Bad Request",
                        status = HttpStatusCode.BadRequest
                    )
                    val order = call.receive<UpdateOrderRequest>()
                    val result = orderService.updateOrderById(id = id, updatedOrder = order)
                    call.respond(result)
                }

                delete {
                    val id: Long = call.parameters["id"]?.toLong() ?: return@delete call.respondText(
                        "Bad Request",
                        status = HttpStatusCode.BadRequest
                    )
                    val deletedOrder = orderService.deleteOrderById(id);
                    call.respond(deletedOrder)
                }
            }

        }
    }
}
