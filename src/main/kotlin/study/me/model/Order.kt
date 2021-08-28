package study.me.model

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDate

object Orders : Table<Order>("orders") {
    val id = long("id").primaryKey().bindTo { it.id }
    val userId = long("user_id").bindTo { it.userId }
    val status = varchar("status").bindTo { it.status }
    val createdAt = date("created_at").bindTo { it.createdAt }
    val updatedAt = date("updated_at").bindTo { it.updatedAt }
}


object OrderItems : Table<OrderItem>("order_items") {
    val id = long("id").primaryKey().bindTo { it.id }
    val itemId = long("item_id").bindTo { it.itemId }

    //    val orderId = long("order_id").references(Orders) { it.order }
    val orderId = long("order_id").bindTo { it.orderId }
    val quantity = int("quantity").bindTo { it.quantity }
}

interface Order : Entity<Order> {
    companion object : Entity.Factory<Order>()

    val id: Long
    val userId: Long
    val status: String
    val createdAt: LocalDate
    val updatedAt: LocalDate
}

interface OrderItem : Entity<OrderItem> {
    companion object : Entity.Factory<OrderItem>()

    val id: Long
    val itemId: Long
    val orderId: Long
    val quantity: Int
}

@Serializable
data class FullOrder(
    var id: Long? = -1,
    var userId: Long? = -1,
    var status: String? = "",
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var orderItems: List<FullOrderItem> = emptyList()
)

@Serializable
data class FullOrderItem(
    val id: Long?,
    val itemId: Long?,
    val quantity: Int?
)