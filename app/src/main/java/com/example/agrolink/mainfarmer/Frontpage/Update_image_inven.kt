package com.example.agrolink.mainfarmer.Frontpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrolink.R
import com.example.agrolink.databinding.ActivityUpdateImageInvenBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import android.util.Log

class UpdateImageInven : AppCompatActivity() {

    lateinit var binding: ActivityUpdateImageInvenBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateImageInvenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Handle system insets (if required)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get product ID passed from the inventory activity
        productId = intent.getStringExtra("PRODUCT_ID")

        binding.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        binding.button.setOnClickListener {
            updateProductImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            if (imageUri != null) {
                binding.imageView.setImageURI(imageUri)
                Log.d("UpdateImageInven", "Selected image URI: $imageUri")
            } else {
                Log.e("UpdateImageInven", "Failed to get image URI")
            }
        }
    }

    private fun updateProductImage() {
        if (imageUri != null && productId != null) {
            val fileName = UUID.randomUUID().toString()
            val storageRef = storage.reference.child("product_images/$fileName")

            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Log the new image URL
                        Log.d("UpdateImageInven", "New Image URL: $imageUrl")

                        // Update the product image URL in Firestore
                        firestore.collection("products").document(productId!!)
                            .update("imageUrl", imageUrl)
                            .addOnSuccessListener {
                                Log.d("UpdateImageInven", "Product image successfully updated")
                                Toast.makeText(this, "Product Image Updated", Toast.LENGTH_SHORT).show()
                                finish() // Close the activity after successful update
                            }
                            .addOnFailureListener { e ->
                                Log.e("UpdateImageInven", "Failed to update product image: ${e.message}")
                                Toast.makeText(this, "Failed to update: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("UpdateImageInven", "Image upload failed: ${e.message}")
                    Toast.makeText(this, "Image Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select an image or invalid product ID", Toast.LENGTH_SHORT).show()
        }
    }
}
