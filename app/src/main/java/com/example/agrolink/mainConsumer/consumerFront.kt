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
import com.example.agrolink.mainConsumer.Profile.MainProfile
import com.example.agrolink.mainConsumer.Profile.MainProfileActivity
import com.example.agrolink.mainfarmer.Profile.ProfileActivity

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

        // Profile button click listener
        binding.profile.setOnClickListener {
            val intent = Intent(this, MainProfileActivity::class.java)
            startActivity(intent)
        }
    }
}