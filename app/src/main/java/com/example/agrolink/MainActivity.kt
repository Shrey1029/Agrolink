package com.example.agrolink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var farmerButton: Button
    lateinit var consumerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        farmerButton = findViewById(R.id.farmer)
        consumerButton = findViewById(R.id.consumer)

        farmerButton.setOnClickListener {
            // Navigate to Loginpage when "Farmer" button is clicked
            navigateToLoginPage()
        }

        consumerButton.setOnClickListener {
            // Navigate to Loginpage when "Consumer" button is clicked
            navigateToLoginPage()
        }
    }

    private fun navigateToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
