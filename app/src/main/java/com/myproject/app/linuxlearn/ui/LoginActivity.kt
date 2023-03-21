@file:Suppress("DEPRECATION")

package com.myproject.app.linuxlearn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.app.linuxlearn.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        initAction()
    }

    private fun initAction() {
        binding?.apply {
            tvRegisterMid.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnLogin.setOnClickListener {
                loginUser()
            }


        }
    }

    private fun loginUser() {
        binding?.apply {
            val email = tilEmail.text.toString()
            val password = tilPassword.text.toString()

            when {
                TextUtils.isEmpty(email) -> {
                    etEmail.error = "Email Can't be Empty!"
                    etEmail.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    etPassword.error = "Password Can't be Empty"
                    etPassword.requestFocus()
                }
                else -> {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login Failed caused by: " + task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}