package com.example.agrolink.mainConsumer.Orders.RecyclerView

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrolink.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CropAdapter(
    private val context: Context,
    private val cropList: List<Crop>,
    private val consumerId: String,
    private val consumerName: String,
    private val consumerPhone: String
) : RecyclerView.Adapter<CropAdapter.CropViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    class CropViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cropName: TextView = itemView.findViewById(R.id.cropName)
        val cropPrice: TextView = itemView.findViewById(R.id.cropPrice)
        val cropDescription: TextView = itemView.findViewById(R.id.cropDescription)
        val cropStock: TextView = itemView.findViewById(R.id.cropStock)
        val orderButton: Button = itemView.findViewById(R.id.orderButton)
        val cropImage: ImageView = itemView.findViewById(R.id.cropImage)
        val farmerProfileImage: ImageView = itemView.findViewById(R.id.farmerProfileImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crop_card_item, parent, false)
        return CropViewHolder(view)
    }

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        val crop = cropList[position]
        holder.cropName.text = crop.productName
        holder.cropPrice.text = "Price: ₹${crop.price}"
        holder.cropDescription.text = "Description: ${crop.description}"
        holder.cropStock.text = "Stock Available: ${crop.stock}"

        // Load crop and farmer images using Glide
        Glide.with(context).load(crop.imageUrl).into(holder.cropImage)
        Glide.with(context).load(crop.farmerProfileImageUrl).into(holder.farmerProfileImage)

        // Order button click listener - open dialog to enter quantity
        holder.orderButton.setOnClickListener {
            showOrderDialog(crop)
        }
    }

    private fun showOrderDialog(crop: Crop) {
        val builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_order, null)

        val quantityInput = dialogView.findViewById<EditText>(R.id.quantityEditText)

        builder.setView(dialogView)
            .setTitle("Enter Quantity")
            .setPositiveButton("Place Order") { _, _ ->
                val quantity = quantityInput.text.toString().trim()

                // Check if quantity is valid
                if (quantity.isNotEmpty() && quantity.toIntOrNull() != null && quantity.toInt() > 0) {
                    val order = Order(
                        orderId = UUID.randomUUID().toString(),
                        cropId = crop.id,  // Ensure crop.id is filled
                        farmerId = crop.userId, // Use the farmer's userId from the crop
                        quantity = quantity,
                        consumerId = consumerId,
                        status = "Pending"
                    )
                    placeOrder(order)
                } else {
                    Toast.makeText(context, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun placeOrder(order: Order) {
        firestore.collection("orders").add(order)
            .addOnSuccessListener {
                Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to place order.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int = cropList.size
}
