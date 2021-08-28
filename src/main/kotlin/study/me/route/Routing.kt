package study.me.route

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.sequenceOf
import study.me.model.OrderItems
import study.me.model.Orders
import study.me.service.getAllOrders
import study.me.service.getOrderById

private val Database.orders get() = this.sequenceOf(Orders)
private val Database.orderItems get() = this.sequenceOf(OrderItems)

fun Application.configureOrderRouting() {
    routing {
        route("/orders") {
            get("") {
                val orders = getAllOrders()
                call.respond(orders)
            }
            get("/{id}") {
                val id: Long = call.parameters["id"]?.toLong() ?: return@get call.respondText(
                    "Bad Request",
                    status = HttpStatusCode.BadRequest
                )
                val order = getOrderById(id)
                if (order.id == -1L) {
                    return@get call.respondText(
                            "No such order with id $id",
                    status = HttpStatusCode.BadRequest
                    )
                }
                call.respond(order)
            }
        }
    }
}
