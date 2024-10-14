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

        // Load orders for this farmer from Firestore
        loadOrdersForFarmer(farmerId)
    }

    private fun loadOrdersForFarmer(farmerId: String) {
        firestore.collection("farmers_orders").document(farmerId)
            .collection("orders")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("LoadOrders", "Error loading orders: ${e.localizedMessage}")
                    Toast.makeText(this, "Error loading orders: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                // Clear the list before adding new data
                ordersList.clear()

                snapshots?.documents?.forEach { document ->
                    val orderData = document.data
                    if (orderData != null) {
                        val order = Order(
                            orderId = document.id,
                            cropId = orderData["cropId"] as? String ?: "",
                            consumerId = orderData["consumerId"] as? String ?: "",
                            quantity = orderData["quantity"] as? String ?: "",
                            amount = (orderData["amount"] as? Number)?.toDouble() ?: 0.0,
                            status = orderData["status"] as? String ?: "Pending"
                        )
                        fetchConsumerDetails(order)
                    } else {
                        Log.e("LoadOrders", "No data found for document: ${document.id}")
                    }
                }
            }
    }

    private fun fetchConsumerDetails(order: Order) {
        firestore.collection("consumers").document(order.consumerId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    order.consumerName = document.getString("name") ?: "Unknown"
                    order.consumerPhone = document.getString("phone") ?: "N/A"
                    ordersList.add(order) // Add the order to the list
                    ordersAdapter.notifyDataSetChanged() // Notify adapter of data changes
                } else {
                    Log.e("FetchConsumer", "No such consumer with ID: ${order.consumerId}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FetchConsumer", "Error fetching consumer details: ${exception.localizedMessage}")
            }
    }
}
