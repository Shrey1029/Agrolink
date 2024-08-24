package com.example.agrolink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class FrontpageActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var menuIcon: ImageView
    private lateinit var searchIcon: ImageView
    private lateinit var invenicon: ImageView
    private lateinit var tickicon: ImageView
    private lateinit var ordericon: ImageView
    private lateinit var infoicon: ImageView
    private lateinit var weathericon: ImageView
    private lateinit var profileicon: ImageView
    private lateinit var mapicon: ImageView
    private lateinit var signicon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) // Ensure this matches your XML filename

        // Initialize FloatingActionButton
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            // Handle FAB click here
        }

        // Initialize toolbar buttons
        menuIcon = findViewById(R.id.menuIcon)
        menuIcon.setOnClickListener {
            // Handle menu icon click here
        }

        searchIcon = findViewById(R.id.searchIcon)
        searchIcon.setOnClickListener {
            // Handle search icon click here
        }

        // Initialize GridLayout buttons
        invenicon = findViewById(R.id.invenicon)
        invenicon.setOnClickListener {
            // Handle Inventory button click here
        }

        tickicon = findViewById(R.id.tickicon)
        tickicon.setOnClickListener {
            // Handle Quality Check button click here
        }

        ordericon = findViewById(R.id.ordericon)
        ordericon.setOnClickListener {
            // Handle Orders button click here
        }

        infoicon = findViewById(R.id.infoicon)
        infoicon.setOnClickListener {
            // Handle Information button click here
        }

        weathericon = findViewById(R.id.weathericon)
        weathericon.setOnClickListener {
            // Handle Weather button click here
        }

        profileicon = findViewById(R.id.profileicon)
        profileicon.setOnClickListener {
            // Handle Profile button click here
        }

        mapicon = findViewById(R.id.mapicon)
        mapicon.setOnClickListener {
            // Handle Map button click here
        }

        signicon = findViewById(R.id.signicon)
        signicon.setOnClickListener {
            // Handle Sign Out button click here
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
