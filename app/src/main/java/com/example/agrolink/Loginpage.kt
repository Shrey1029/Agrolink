package com.example.agrolink

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.agrolink.databinding.ActivityLoginpageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginpageBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Customize Google Sign-In button
        val textOfGoogleButton = binding.signInButton.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with Google"
        textOfGoogleButton.setTextColor(Color.WHITE)
        textOfGoogleButton.textSize = 18F

        // Initialize Google Sign-In options
        signInGoogle()

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up ActivityResultLauncher
        googleSignInResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                firebaseSignInWithGoogle(task)
            }
        }

        // Set click listener for login button
        binding.loginButton.setOnClickListener {
            val emailText = binding.email.text.toString()
            val passwordText = binding.password.text.toString()
            // Handle email/password login
            firebaseAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Navigate to FrontpageActivity on successful login
                    val intent = Intent(this, FrontpageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set click listener for register text
        binding.registerText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for Google Sign-In button
        binding.signInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInResultLauncher.launch(signInIntent)
    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to AgroLink", Toast.LENGTH_SHORT).show()

            // Navigate to FrontpageActivity on successful Google Sign-In
            val intent = Intent(this, FrontpageActivity::class.java)
            startActivity(intent)
            finish()

            firebaseGoogleAccount(account)
        } catch (e: ApiException) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle successful sign-in
                val user = firebaseAuth.currentUser
                // Update UI with the signed-in user's information, if needed
            } else {
                Toast.makeText(applicationContext, "Sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
