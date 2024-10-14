package com.example.agrolink.mainConsumer.Profile
data class MainProfile(
    val name: String? = null,
    val age: String? = null,
    val email: String? = null,
    val location: String? = null,
    val phone: String? = null,
    val address: String? = null,  // Add this line to include address
    val imageUrl: String? = null
)
