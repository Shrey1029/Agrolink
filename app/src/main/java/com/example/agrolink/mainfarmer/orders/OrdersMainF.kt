package com.example.agrolink.mainfarmer.orders

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrolink.databinding.ActivityOrdersMainFBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrdersMainF : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersMainFBinding
    private lateinit var ordersList: MutableList<Order>
    private lateinit var ordersAdapter: FarmerOrdersAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var farmerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityOrdersMainFBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        farmerId = auth.currentUser?.uid ?: ""

        // Set up RecyclerView
        ordersList = mutableListOf()
        ordersAdapter = FarmerOrdersAdapter(this, ordersList)
        binding.farmerOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.farmerOrdersRecyclerView.adapter = ordersAdapter

        // Load orders from Firestore
        loadOrdersForFarmer()
    }

    private fun loadOrdersForFarmer() {
        // Correctly querying the farmers_orders collection
        firestore.collection("farmers_orders")
            .whereEqualTo("farmerId", farmerId) // Filter by farmerId
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error loading orders: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Firestore", "Error: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    ordersList.clear()
                    for (document in snapshots.documents) {
                        val order = document.toObject(Order::class.java)
                        if (order != null) {
                            ordersList.add(order)
                        } else {
                            Log.e("Firestore", "Invalid order data: ${document.id}")
                        }
                    }
                    ordersAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Firestore", "No orders found")
                    Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


