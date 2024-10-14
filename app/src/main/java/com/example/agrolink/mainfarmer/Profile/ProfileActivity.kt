package com.example.agrolink.mainfarmer.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.agrolink.R
import com.example.agrolink.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val EDIT_PROFILE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Fetch data from Firestore
        fetchProfileData()

        // Set click listener for Edit button
        binding.editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh profile data after returning from EditProfileActivity
            fetchProfileData()
        }
    }

    private fun fetchProfileData() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("farmers_profile").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(Profile::class.java)
                    binding.farmerName.text = "Name: ${profile?.name ?: "N/A"}"
                    binding.farmerAge.text = "Age: ${profile?.age ?: "N/A"}"
                    binding.farmerLocation.text = "Location: ${profile?.location ?: "N/A"}"
                    binding.farmerExperience.text = "Experience: ${profile?.experience ?: "N/A"}"
                    binding.farmerPhone.text = "Phone Number: ${profile?.phoneNumber ?: "N/A"}"  // New phone number field

                    // Load profile image
                    val imageUrl = profile?.imageUrl
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(imageUrl)
                            .into(binding.profileImage)
                    } else {
                        Log.d("ProfileActivity", "No image URL found")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileActivity", "Error fetching data", exception)
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
    }


}
