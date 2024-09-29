package com.example.agrolink.mainConsumer.Profile

data class MainProfile(
    val name: String? = null,     // Nullable types to handle missing fields
    val email: String? = null,
    val location: String? = null,
    val phone: String? = null,
    val imageUrl: String? = null  // Make sure Firestore document has this field if you're loading it
)
