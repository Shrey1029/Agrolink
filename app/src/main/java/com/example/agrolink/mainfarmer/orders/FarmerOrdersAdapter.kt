package com.example.agrolink.mainfarmer.orders

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.agrolink.R
import com.google.firebase.database.FirebaseDatabase

class FarmerOrdersAdapter(private val context: Context, private val ordersList: List<Order>) :
    RecyclerView.Adapter<FarmerOrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val consumerName: TextView = itemView.findViewById(R.id.consumerName)
        val consumerPhone: TextView = itemView.findViewById(R.id.consumerPhone)
        val cropName: TextView = itemView.findViewById(R.id.cropName)
        val orderQuantity: TextView = itemView.findViewById(R.id.orderQuantity)
        val orderAmount: TextView = itemView.findViewById(R.id.orderAmount)
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val acceptOrderButton: Button = itemView.findViewById(R.id.acceptOrderButton)
        val rejectOrderButton: Button = itemView.findViewById(R.id.rejectOrderButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ordered_farmer, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]

        // Bind order details to the view
        holder.consumerName.text = order.consumerName
        holder.consumerPhone.text = "Phone: ${order.consumerPhone}"
        holder.cropName.text = "Crop: ${order.cropId}" // Change to crop name if available
        holder.orderQuantity.text = "Quantity: ${order.quantity}"
        holder.orderAmount.text = "Total Amount: ₹${order.amount}"
        holder.orderStatus.text = "Status: ${order.status}"

        // Handle button click events
        holder.acceptOrderButton.setOnClickListener {
            updateOrderStatus(order, "Accepted")
        }

        holder.rejectOrderButton.setOnClickListener {
            updateOrderStatus(order, "Rejected")
        }
    }

    override fun getItemCount(): Int = ordersList.size

    private fun updateOrderStatus(order: Order, newStatus: String) {
        val databaseReference = FirebaseDatabase.getInstance()
            .getReference("farmers_orders/${order.farmerId}/orders/${order.orderId}")

        databaseReference.child("status").setValue(newStatus)
            .addOnSuccessListener {
                Toast.makeText(context, "Order $newStatus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to update status: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("OrderUpdate", "Error updating status: ${e.message}")
            }
    }
}
