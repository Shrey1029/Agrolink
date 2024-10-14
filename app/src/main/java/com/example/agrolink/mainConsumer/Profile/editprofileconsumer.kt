package com.example.agrolink.mainConsumer.Profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.agrolink.R
import com.example.agrolink.databinding.ActivityEditprofileconsumerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditConsumerProfileActivity : Activity() {

    private lateinit var binding: ActivityEditprofileconsumerBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    private val userId: String
        get() = auth.currentUser?.uid ?: "default_user_id" // Fallback ID for testing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityEditprofileconsumerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firestore, Authentication, and Firebase Storage
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        // Ensure user is authenticated
        if (auth.currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Load existing data
        loadProfileData()

        // Set up image selection
        binding.editProfileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Set up save button
        binding.saveProfileButton.setOnClickListener {
            saveProfileData()
        }
        binding.returnIcon.setOnClickListener{
            finish()
        }
    }

    private fun loadProfileData() {
        firestore.collection("consumers_profile").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val name = documentSnapshot.getString("name") ?: ""
                    val age = documentSnapshot.getString("age") ?: ""
                    val address = documentSnapshot.getString("address") ?: ""
                    val location = documentSnapshot.getString("location") ?: ""
                    val phone = documentSnapshot.getString("phone") ?: ""
                    val email = documentSnapshot.getString("email") ?: ""
                    val imageUrl = documentSnapshot.getString("imageUrl") ?: ""

                    Log.d("ProfileData", "Fetched profile data: $name, $age, $address, $location, $phone, $email, $imageUrl")

                    binding.editConsumerName.setText(name)
                    binding.editConsumerAge.setText(age)
                    binding.editConsumerAddress.setText(address)
                    binding.editConsumerLocation.setText(location)
                    binding.editConsumerPhone.setText(phone)
                    binding.editConsumerEmail.setText(email)

                    // Load profile image using Glide
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder) // Use your placeholder image
                        .into(binding.editProfileImage)
                } else {
                    Log.e("EditConsumerProfile", "Document does not exist")
                }
            }
            .addOnFailureListener {
                Log.e("EditConsumerProfile", "Error loading profile data", it)
            }
    }


    private fun saveProfileData() {
        val name = binding.editConsumerName.text.toString()
        val age = binding.editConsumerAge.text.toString()
        val address = binding.editConsumerAddress.text.toString()
        val location = binding.editConsumerLocation.text.toString()
        val phone = binding.editConsumerPhone.text.toString()
        val email = binding.editConsumerEmail.text.toString()

        // First, check if the user selected a new image
        if (imageUri != null) {
            uploadImageToFirebaseStorage { downloadUrl ->
                // After the image is uploaded, save the profile data including the image URL
                saveProfileToFirestore(name, age, address, location, phone, email, downloadUrl)
            }
        } else {
            // If no new image was selected, save the profile without changing the image URL
            saveProfileToFirestore(name, age, address, location, phone, email, null)
        }
    }

    private fun uploadImageToFirebaseStorage(onSuccess: (String) -> Unit) {
        val storageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                Log.d("ImageUpload", "Image uploaded successfully.")
                // Get the download URL
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("ImageUpload", "Download URL: $uri")
                    onSuccess(uri.toString())
                }.addOnFailureListener { e ->
                    Log.e("ImageUpload", "Failed to get download URL", e)
                    Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { e ->
                Log.e("ImageUpload", "Failed to upload image", e)
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveProfileToFirestore(
        name: String,
        age: String,
        address: String,
        location: String,
        phone: String,
        email: String,
        imageUrl: String?
    ) {
        val profileData = mutableMapOf(
            "name" to name,
            "age" to age,
            "address" to address,
            "location" to location,
            "phone" to phone,
            "email" to email
        )

        // If imageUrl is not null, add it to the profile data
        imageUrl?.let { profileData["imageUrl"] = it }

        firestore.collection("consumers_profile").document(userId).set(profileData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setResult(Activity.RESULT_OK) // Set result to indicate success
                    finish()  // Close the activity
                } else {
                    // Handle the error
                    Log.e("EditConsumerProfile", "Error saving profile data", task.exception)
                    Toast.makeText(this, "Error saving data: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.editProfileImage.setImageURI(imageUri)
        }
    }
}
