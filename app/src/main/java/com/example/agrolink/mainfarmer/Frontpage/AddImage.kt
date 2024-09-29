package com.example.agrolink.mainfarmer.Frontpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrolink.databinding.ActivityAddImageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import android.util.Log

class AddImage : AppCompatActivity() {

    lateinit var binding: ActivityAddImageBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseFirestore.setLoggingEnabled(true)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Select image from gallery
        binding.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        // Upload data and image to Firebase
        binding.button.setOnClickListener {
            uploadProduct()
        }
        binding.returnIcon.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            if (imageUri != null) {
                binding.imageView.setImageURI(imageUri)
                Log.d("AddImage", "Selected image URI: $imageUri")
            } else {
                Log.e("AddImage", "Failed to get image URI")
            }
        }
    }

    private fun uploadProduct() {
        val productName = binding.Name.text.toString().trim()
        val stock = binding.stock.text.toString().trim()
        val price = binding.price.text.toString().trim()
        val description = binding.description.text.toString().trim()

        if (productName.isEmpty() || stock.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri != null) {
            val fileName = UUID.randomUUID().toString()
            val storageRef = storage.reference.child("product_images/$fileName")

            storageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Log the image URL
                        Log.d("AddImage", "Image URL: $imageUrl")

                        val productData = hashMapOf(
                            "productName" to productName,
                            "stock" to stock,
                            "price" to price,
                            "imageUrl" to imageUrl,
                            "description" to description
                        )

                        // Upload the product data to Firestore and get the product ID
                        firestore.collection("products")
                            .add(productData)
                            .addOnSuccessListener { documentReference ->
                                val productId = documentReference.id // Retrieve the productId here
                                Log.d(
                                    "AddImage",
                                    "Product successfully uploaded with ID: $productId"
                                )
                                Toast.makeText(this, "Product Uploaded", Toast.LENGTH_SHORT).show()
                                finish() // Finish the activity after success
                            }
                            .addOnFailureListener { e ->
                                Log.e("AddImage", "Failed to upload product: ${e.message}")
                                Toast.makeText(
                                    this,
                                    "Failed to upload: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("AddImage", "Image upload failed: ${e.message}")
                    Toast.makeText(this, "Image Upload Failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }
}