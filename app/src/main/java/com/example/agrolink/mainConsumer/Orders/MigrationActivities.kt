package com.example.agrolink.mainConsumer.Orders

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.agrolink.R
import com.google.firebase.firestore.FirebaseFirestore

class MigrationActivities : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_migration)

        updateOrderAmounts()
    }

    private fun updateOrderAmounts() {
        val firestore = FirebaseFirestore.getInstance()
        val ordersRef = firestore.collection("farmers_orders").document("PqFIY7cqUSTZhPlO1UCQJybudRs2").collection("orders")

        ordersRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val amountString = document.getString("amount")
                val amountDouble = amountString?.replace(",", "")?.toDoubleOrNull() ?: 0.0

                // Update the amount field to be a double
                document.reference.update("amount", amountDouble)
                    .addOnSuccessListener {
                        Log.d("UpdateAmount", "Successfully updated amount for order ${document.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("UpdateAmount", "Error updating amount for order ${document.id}", e)
                    }
            }
        }.addOnFailureListener { e ->
            Log.e("UpdateAmount", "Error fetching orders: ", e)
        }
    }
}
