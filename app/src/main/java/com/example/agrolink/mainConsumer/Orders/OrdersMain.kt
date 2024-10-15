package com.example.agrolink.mainConsumer.Orders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrolink.R
import com.example.agrolink.databinding.ActivityOrdersMainBinding
import com.example.agrolink.mainConsumer.Orders.RecyclerView.Crop
import com.example.agrolink.mainConsumer.Orders.RecyclerView.CropAdapter
import com.example.agrolink.signing.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrdersMain : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersMainBinding
    private lateinit var cropAdapter: CropAdapter
    private lateinit var cropList: MutableList<Crop>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore

        val currentUser = auth.currentUser
        val consumerId = currentUser?.uid

        if (consumerId != null) {
            fetchConsumerDetails(consumerId)
        } else {
            signOut()
        }

        binding.returnIcon.setOnClickListener {
            finish()
        }
    }

    private fun fetchConsumerDetails(consumerId: String) {
        firestore.collection("consumers_profile").document(consumerId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val consumerName = documentSnapshot.getString("name") ?: "Unknown"
                    val consumerPhone = documentSnapshot.getString("phone") ?: "Unknown"
                    setupRecyclerView(consumerId, consumerName, consumerPhone)
                } else {
                    Toast.makeText(this, "Consumer details not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching consumer details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupRecyclerView(consumerId: String, consumerName: String, consumerPhone: String) {
        cropList = mutableListOf()
        cropAdapter = CropAdapter(this, cropList, consumerId, consumerName, consumerPhone)
        binding.cropRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cropRecyclerView.adapter = cropAdapter
        loadCrops()
    }

    private fun loadCrops() {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                cropList.clear()
                for (document in result) {
                    val crop = Crop(
                        id = document.id,  // Assign Firestore document ID to Crop ID
                        productName = document.getString("productName") ?: "",
                        price = document.getString("price") ?: "",
                        description = document.getString("description") ?: "",
                        stock = document.getString("stock") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        farmerProfileImageUrl = document.getString("farmerProfileImageUrl") ?: "",
                        userId = document.getString("userId") ?: ""
                    )
                    cropList.add(crop)

                    // Log for debugging
                    Log.d("CropData", "Loaded Crop ID: ${crop.id}, Name: ${crop.productName}")
                }
                cropAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("LoadCrops", "Error loading crops: ${exception.message}")
            }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
