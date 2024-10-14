package com.example.agrolink.mainConsumer.Orders.RecyclerView

data class Order(
    val orderId: String = "",
    val cropId: String = "",
    val quantity: String = "",
    val farmerId: String = "",
    val consumerId: String = "",
    val status: String = "Pending"
)
