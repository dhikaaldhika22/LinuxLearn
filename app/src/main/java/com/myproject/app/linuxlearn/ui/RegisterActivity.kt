package com.myproject.app.linuxlearn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.myproject.app.linuxlearn.Constant
import com.myproject.app.linuxlearn.data.model.UserModel
import com.myproject.app.linuxlearn.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        initAction()
    }

    private fun initAction() {
        binding?.apply {
            tvRegisterMid.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            btnRegister.setOnClickListener {
                createAccount()
            }
        }
    }

    private fun createAccount() {
        binding?.apply {
            val username = tilUsername.text.toString()
            val email = tilEmail.text.toString()
            val password = tilPassword.text.toString()

            when {
                TextUtils.isEmpty(username) -> {
                    etUsername.error = "Username harus diisi!"
                    etUsername.requestFocus()
                }
                TextUtils.isEmpty(email) -> {
                    etEmail.error = "Email harus diisi!"
                    etEmail.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    etPassword.error = "Password harus diisi!"
                    etPassword.requestFocus()
                }
                else -> {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@RegisterActivity) { task ->
                            database = FirebaseDatabase.getInstance(Constant.base_url)
                                .getReference("users")
                            val user = UserModel(tilUsername.text.toString(), tilEmail.text.toString())
                            database.child(task.result.user?.uid.orEmpty()).child("username").setValue(user.username)
                            database.child(task.result.user?.uid.orEmpty()).child("email").setValue(user.email)
                            database.child(task.result.user?.uid.orEmpty()).child("photo").setValue("")


                            if (task.isSuccessful) {
                                Toast.makeText(this@RegisterActivity, "Akun berhasil terdaftar", Toast.LENGTH_SHORT)
                                    .show()

                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "Registration Failed Caused by: " + task.exception?.message,
                                    Toast.LENGTH_SHORT).show()
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