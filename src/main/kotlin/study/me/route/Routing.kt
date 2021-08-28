package study.me.route

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.entity.sequenceOf
import study.me.db.DatabaseConfig
import study.me.model.OrderItems
import study.me.model.Orders

private val Database.orders get() = this.sequenceOf(Orders)
private val Database.orderItems get() = this.sequenceOf(OrderItems)

fun Application.configureOrderRouting() {
    routing {
        route("/orders") {
            get("") {
                val dbConfig = DatabaseConfig()
                val db = dbConfig.initDb()

                var orderIds = db.from(Orders)
                    .select(Orders.id)
                    .map { it[Orders.id] }

                val orders = dbConfig.mapToOrders(orderIds)

                call.respond(orders)
            }
            get("/{id}") {
                val id: Long = call.parameters["id"]?.toLong() ?: -1
                val dbConfig = DatabaseConfig()
                val db = dbConfig.initDb()

                val orderQuery = db.from(Orders)
                    .select()
                    .where { Orders.id eq id }

                val orderItemsQuery = db.from(OrderItems)
                    .select()
                    .where(OrderItems.orderId eq id)

                val order = dbConfig.mapToOrder(orderQuery, orderItemsQuery)

                call.respond(order)
            }
        }
    }
}
