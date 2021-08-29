package study.me.service

import org.ktorm.database.Database
import org.ktorm.dsl.*
import study.me.dto.request.UpdateOrderItemsRequest
import study.me.dto.request.UpdateOrderRequest
import study.me.mapper.mapToOrder
import study.me.mapper.mapToOrders
import study.me.model.FullOrder
import study.me.model.OrderItems
import study.me.model.Orders
import java.time.LocalDate

class OrderService(private val db: Database) {

    fun getAllOrders(): List<FullOrder> {
        val orderIds = db.from(Orders)
            .select(Orders.id)
            .map { it[Orders.id] }

        return mapToOrders(orderIds)
    }

    fun getOrderById(id: Long): FullOrder {
        val orderQuery = db.from(Orders)
            .select()
            .where { Orders.id eq id }

        val orderItemsQuery = db.from(OrderItems)
            .select()
            .where(OrderItems.orderId eq id)

        return mapToOrder(orderQuery, orderItemsQuery)
    }

    fun updateOrderById(id: Long, updatedOrder: UpdateOrderRequest): FullOrder {
        db.update(Orders) {
            set(it.userId, updatedOrder.userId)
            set(it.status, updatedOrder.status.uppercase())
            set(it.updatedAt, LocalDate.now())
            where { it.id eq id }
        }
        updatedOrder.orderItems.forEach { oi ->
            if (oi.id == null) {
                createNewOrderItem(id, oi)
            } else {
                updateOrderItem(oi)
            }
        }

        return getOrderById(id)
    }

    private fun createNewOrderItem(orderId: Long, orderItem: UpdateOrderItemsRequest) {
        db.insert(OrderItems) {
            set(it.itemId, orderItem.itemId)
            set(it.quantity, orderItem.quantity)
            set(it.orderId, orderId)
        }
    }

    private fun updateOrderItem(orderItem: UpdateOrderItemsRequest) {
        db.update(OrderItems) {
            set(it.itemId, orderItem.itemId)
            set(it.quantity, orderItem.quantity)
            where { it.id eq orderItem.id!! }
        }
    }

    fun deleteOrderById(id: Long): FullOrder {
        val order = getOrderById(id)
        // TODO Add transaction
        db.delete(OrderItems) { it.orderId eq id }
        db.delete(Orders) { it.id eq id }
        return order
    }

}