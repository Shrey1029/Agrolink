package com.example.agrolink.mainfarmer.orders

data class Order(
    val orderId: String = "",
    val cropId: String = "",
    val farmerId: String = "",
    val consumerId: String = "",
    var consumerName: String = "",
    var consumerPhone: String = "",
    var quantity: String = "", // Store quantity as a String
    var amount: Double = 0.0, // Store amount as a Double
    val status: String = "Pending" // Order status can be "Pending", "Accepted", "Rejected", etc.
)
