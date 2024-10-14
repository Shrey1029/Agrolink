package com.example.agrolink.mainConsumer.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.agrolink.databinding.ActivityMainProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val EDIT_PROFILE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityMainProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Fetch consumer profile data from Firestore
        fetchProfileData()

        // Set click listener for Edit button
        binding.editProfileButton.setOnClickListener {
            val intent = Intent(
                this,
                EditConsumerProfileActivity::class.java
            ) // Navigate to Edit Profile Activity
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh profile data after returning from EditConsumerProfileActivity
            fetchProfileData()
        }
    }

    private fun fetchProfileData() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("consumers_profile").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(MainProfile::class.java)
                    Log.d("MainProfileActivity", "Fetched profile data: $profile")

                    binding.consumerName.text = "Name: ${profile?.name ?: "N/A"}"
                    binding.consumerAge.text = "Age: ${profile?.age ?: "N/A"}"  // No need to convert if age is a String
                    binding.Email.text = "Email: ${profile?.email ?: "N/A"}"
                    binding.Location.text = "Location: ${profile?.location ?: "N/A"}"
                    binding.Phone.text = "Phone: ${profile?.phone ?: "N/A"}"
                    binding.address.text = "Address: ${profile?.address ?: "N/A"}"

                    // Load profile image
                    val imageUrl = profile?.imageUrl
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(imageUrl)
                            .into(binding.profileImage)
                    } else {
                        Log.d("MainProfileActivity", "No image URL found")
                    }
                } else {
                    Log.d("MainProfileActivity", "Profile document does not exist")
                    Toast.makeText(this, "Profile does not exist", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MainProfileActivity", "Error fetching data", exception)
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
    }


}