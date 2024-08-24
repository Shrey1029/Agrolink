package com.example.agrolink

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrolink.databinding.ActivitySignUpactivityBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpactivityBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for Sign Up button
        binding.signUpButton.setOnClickListener {
            val emailText = binding.email.text.toString()
            val passwordText = binding.password.text.toString()
            val confirmPasswordText = binding.confirmPassword.text.toString()

            // Handle sign-up logic
            if (passwordText == confirmPasswordText) {
                signUp(emailText, passwordText)
            } else {
                // Show an error message for password mismatch
                binding.confirmPassword.error = "Passwords do not match"
            }
        }

        // Set click listener for login text
        binding.loginText.setOnClickListener {
            // Finish the activity and go back to the Login page
            finish()
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-up success, notify user
                    Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
                    // Finish the activity to return to LoginActivity
                    finish()
                } else {
                    // Sign-up failed, show error message
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
