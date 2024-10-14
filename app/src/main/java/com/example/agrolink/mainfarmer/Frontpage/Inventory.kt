package com.example.agrolink.mainfarmer.Frontpage

import Product
import ProductAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrolink.databinding.ActivityInventory2Binding
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Inventory : AppCompatActivity() {

    lateinit var binding: ActivityInventory2Binding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ProductAdapter
    private var productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventory2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        adapter = ProductAdapter(productList) { product ->
            val intent = Intent(this, UpdateImageInven::class.java)
            intent.putExtra("PRODUCT_ID", product.productID) // Pass product ID
            startActivity(intent)
        }
        binding.returnIcon.setOnClickListener {
            finish()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        fetchProducts()

        // Add new product
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddImage::class.java)
            startActivity(intent)
        }
    }

    private fun fetchProducts() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        firestore.collection("products")
            .whereEqualTo("userId", userId) // Filter products by the current user's ID
            .get()
            .addOnSuccessListener { documents ->
                productList.clear()
                for (document in documents) {
                    val productName = document.getString("productName") ?: ""
                    val stock = document.getString("stock") ?: ""
                    val price = document.getString("price") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val description = document.getString("description") ?: ""
                    val productID = document.id
                    val product =
                        Product(productName, stock, price, imageUrl, description, productID)
                    productList.add(product)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching products", Toast.LENGTH_SHORT).show()
            }
    }
}