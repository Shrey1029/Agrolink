package com.example.agrolink.mainConsumer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrolink.R
import com.example.agrolink.databinding.ActivityConsumerFrontBinding
import com.example.agrolink.databinding.ActivityProfileBinding
import com.example.agrolink.mainConsumer.Orders.OrdersMain
import com.example.agrolink.mainConsumer.Profile.MainProfile
import com.example.agrolink.mainConsumer.Profile.MainProfileActivity
import com.example.agrolink.mainfarmer.Profile.ProfileActivity
import com.example.agrolink.signing.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class consumerFront : AppCompatActivity() {
    lateinit var binding: ActivityConsumerFrontBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerFrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the OnApplyWindowInsetsListener here, outside the click listener
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            // Handle window insets if necessary
            insets
        }
        binding.invenicon.setOnClickListener{
            val intent = Intent(this, OrdersMain::class.java)
            startActivity(intent)
        }
        binding.returnIcon.setOnClickListener{
            finish()
        }

        // Profile button click listener
        binding.profile.setOnClickListener {
            val intent = Intent(this, MainProfileActivity::class.java)
            startActivity(intent)
        }
        binding.signicon.setOnClickListener{
            signOut()
        }
    }
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        // Navigate back to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}