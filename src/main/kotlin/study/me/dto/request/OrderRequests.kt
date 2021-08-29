package study.me.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderRequest(
    val userId: Long,
    val status: String,
    val orderItems: List<UpdateOrderItemsRequest>
)

@Serializable
data class UpdateOrderItemsRequest(
    val id: Long? = null,
    val itemId: Long,
    val quantity: Int
)