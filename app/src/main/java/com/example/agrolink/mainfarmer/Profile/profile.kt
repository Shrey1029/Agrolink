package com.example.agrolink.mainfarmer.Profile

data class Profile(
    val name: String = "",
    val age: String = "",
    val location: String = "",
    val experience: String = "",
    val phoneNumber: String = "",  // New field for phone number
    val imageUrl: String = "" // URL for profile image
)
