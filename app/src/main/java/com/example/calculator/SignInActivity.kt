package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calculator.databinding.ActivitySignInBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val auth by lazy {
        Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListener()
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            navigateToMainActivity()
        }
    }

    private fun setUpListener() {
        binding.signInBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val regex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
                if (email.matches(regex)) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(
                                this,
                                "Authentication failed please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Please enter correct email",Toast.LENGTH_SHORT).show()
                }
            } else {
                 Toast.makeText(this,"Please fill empty field",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}