package com.myproject.app.linuxlearn.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.app.linuxlearn.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        auth = Firebase.auth

        initAction()
    }

    private fun initAction() {
        binding?.apply {
            ivLogo.alpha = 0f
            ivLogo.animate().setDuration(2000).alpha(1f).withEndAction {
                checkUser()
                finish()
            }
        }
    }

    private fun checkUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashScreenActivity, StartedScreenActivity::class.java))
        }
    }
}