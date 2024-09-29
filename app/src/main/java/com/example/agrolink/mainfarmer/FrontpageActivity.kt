package com.example.agrolink.mainfarmer


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.agrolink.signing.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.example.agrolink.mainfarmer.Frontpage.Inventory
import com.example.agrolink.mainfarmer.Profile.ProfileActivity
import com.example.agrolink.R


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
            val intent= Intent(this, Inventory::class.java)
            startActivity(intent)
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
            val intent=Intent(this, ProfileActivity::class.java)
            startActivity(intent)
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
