package com.myproject.app.linuxlearn.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.navView?.setItemSelected(R.id.nav_home, true)
        auth = Firebase.auth

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, HomeFragment())
            .commit()

        supportActionBar?.hide()
        navBottom()
    }

    private fun navBottom() {
        binding?.navView?.setOnItemSelectedListener {
            var fragment: Fragment? = null
            fragment = when (it) {
                R.id.nav_exercise -> ExerciseFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}