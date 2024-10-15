package com.example.agrolink.mainfarmer.orders

data class Order(
    val orderId: String = "",
    val cropId: String = "",
    val farmerId: String = "",
    val consumerId: String = "",
    val consumerName: String = "",
    val consumerPhone: String = "",
    var quantity: String = "", // Keep this as a String if you want to store it as such
    var amount: Double = 0.0, // Change this to Double for calculations
    val status: String = "Pending" // Order status can be "Pending", "Accepted", "Rejected", etc.
)