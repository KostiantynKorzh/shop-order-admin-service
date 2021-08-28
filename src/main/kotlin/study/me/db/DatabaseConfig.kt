package study.me.db

import org.ktorm.database.Database
import org.ktorm.dsl.*
import study.me.model.FullOrder
import study.me.model.FullOrderItem
import study.me.model.OrderItems
import study.me.model.Orders


class DatabaseConfig {

    fun initDb(): Database {
        val host = System.getenv("MYSQL_HOST")
        return Database.connect(
            "jdbc:mysql://$host:3306/orders_db?useSSL=false",
            user = "root", password = System.getenv("MYSQL_ROOT_PASSWORD")
        )
    }

    fun mapToOrder(orderQuery: Query, orderItemsQuery: Query): FullOrder {
        var orderItems: MutableList<FullOrderItem> = mutableListOf()
        var fullOrder = FullOrder()

        orderQuery.forEach { row ->
            fullOrder = FullOrder(
                id = row[Orders.id],
                userId = row[Orders.userId],
                status = row[Orders.status],
                createdAt = row[Orders.createdAt].toString(),
                updatedAt = row[Orders.updatedAt].toString(),
                orderItems = emptyList()
            )
        }

        orderItemsQuery.forEach { row ->
            orderItems.add(
                FullOrderItem(
                    id = row[OrderItems.id],
                    itemId = row[OrderItems.itemId],
                    quantity = row[OrderItems.quantity],
                )
            )
        }

        fullOrder.orderItems = orderItems

        return fullOrder
    }

    fun mapToOrders(orderIds: List<Long?>): List<FullOrder> {
        var fullOrders: MutableList<FullOrder> = mutableListOf()



        orderIds.forEach { id ->
            val dbConfig = DatabaseConfig()
            val db = dbConfig.initDb()

            val orderQuery = db.from(Orders)
                .select()
                .where { Orders.id eq id!! }

            val orderItemsQuery = db.from(OrderItems)
                .select()
                .where(OrderItems.orderId eq id!!)

            fullOrders.add(mapToOrder(orderQuery, orderItemsQuery))
        }

        return fullOrders
    }
}