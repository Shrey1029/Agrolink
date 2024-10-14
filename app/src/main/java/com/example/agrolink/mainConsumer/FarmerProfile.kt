package com.example.agrolink.mainConsumer.FarmerProfile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.agrolink.R
import com.example.agrolink.databinding.ActivityFarmerProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class FarmerProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityFarmerProfileBinding // View Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmerProfileBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the binding root

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Get farmer ID from intent
        val farmerId = intent.getStringExtra("farmerId")

        // Load farmer data
        if (farmerId != null) {
            loadFarmerProfile(farmerId)
        }

        // Set click listener for return icon if needed
        binding.returnIcon.setOnClickListener {
            finish() // Close the activity
        }
    }

    private fun loadFarmerProfile(farmerId: String) {
        firestore.collection("farmers_profile").document(farmerId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val name = documentSnapshot.getString("name") ?: "N/A"
                    val age = documentSnapshot.getString("age") ?: "N/A"
                    val location = documentSnapshot.getString("location") ?: "N/A"
                    val experience = documentSnapshot.getString("experience") ?: "N/A"
                    val phoneNumber = documentSnapshot.getString("phoneNumber") ?: "N/A"
                    val imageUrl = documentSnapshot.getString("imageUrl")

                    // Set data to the views using binding
                    binding.farmerName.text = name
                    binding.farmerAge.text = age
                    binding.farmerLocation.text = location
                    binding.farmerExperience.text = experience
                    binding.farmerPhone.text = phoneNumber

                    // Load profile image using Glide
                    if (imageUrl != null) {
                        Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder)
                            .into(binding.farmerProfileImage)
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace() // Handle error
            }
    }
}
