package com.example.agrolink.mainConsumer.Orders.RecyclerView

data class Crop(
    val id: String = "",
    val productName: String = "",
    val price: String = "",
    val description: String = "",
    val stock: String = "",
    val imageUrl: String = "", // URL of the crop image
    val farmerProfileImageUrl: String = "", // URL of the farmer's profile image
    val userId: String = "" // Farmer's user ID
)
