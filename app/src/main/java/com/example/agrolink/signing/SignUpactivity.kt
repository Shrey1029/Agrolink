package com.example.agrolink.signing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrolink.databinding.ActivitySignUpactivityBinding
import com.example.agrolink.mainConsumer.consumerFront
import com.example.agrolink.mainfarmer.FrontpageActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpactivityBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var role: String? = null  // To store role passed from LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the role passed from LoginActivity
        role = intent.getStringExtra("ROLE")

        // Set click listener for Sign Up button
        binding.signUpButton.setOnClickListener {
            val emailText = binding.email.text.toString()
            val passwordText = binding.password.text.toString()
            val confirmPasswordText = binding.confirmPassword.text.toString()

            if (passwordText == confirmPasswordText) {
                signUp(emailText, passwordText)
            } else {
                binding.confirmPassword.error = "Passwords do not match"
            }
        }

        // Set click listener for login text
        binding.loginText.setOnClickListener {
            finish()  // Return to LoginActivity
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
                    navigateBasedOnRole()
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateBasedOnRole() {
        if (role == "Farmer") {
            val intent = Intent(this, FrontpageActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, consumerFront::class.java)
            startActivity(intent)
        }
    }
}
