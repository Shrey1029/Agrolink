package com.example.agrolink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.agrolink.databinding.ActivityMainBinding
import com.example.agrolink.signing.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Farmer button click
        binding.farmer.setOnClickListener {
            navigateToLoginPage("Farmer")
        }

        // Handle Consumer button click
        binding.consumer.setOnClickListener {
            navigateToLoginPage("Consumer")
        }
    }

    private fun navigateToLoginPage(role: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("ROLE", role)  // Pass the selected role to the LoginActivity
        startActivity(intent)
    }
}
