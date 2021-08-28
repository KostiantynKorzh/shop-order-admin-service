package study.me.service

import org.ktorm.dsl.*
import study.me.db.DatabaseConfig
import study.me.mapper.mapToOrder
import study.me.mapper.mapToOrders
import study.me.model.FullOrder
import study.me.model.OrderItems
import study.me.model.Orders

fun getAllOrders(): List<FullOrder> {
    val dbConfig = DatabaseConfig()
    val db = dbConfig.initDb()

    val orderIds = db.from(Orders)
        .select(Orders.id)
        .map { it[Orders.id] }

    return mapToOrders(orderIds)
}

fun getOrderById(id: Long) :FullOrder{
    val dbConfig = DatabaseConfig()
    val db = dbConfig.initDb()

    val orderQuery = db.from(Orders)
        .select()
        .where { Orders.id eq id }

    val orderItemsQuery = db.from(OrderItems)
        .select()
        .where(OrderItems.orderId eq id)

    return mapToOrder(orderQuery, orderItemsQuery)
}