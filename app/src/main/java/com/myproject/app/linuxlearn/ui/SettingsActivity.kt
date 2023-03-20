package com.myproject.app.linuxlearn.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private var switch: SwitchMaterial? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        initAction()
        setToolbar(getString(R.string.settings))
        changeTheme()
    }

    private fun initAction() {
        binding?.apply {
            btnLogout.setOnClickListener {
                logOut()
            }

            cdChangeAccount.setOnClickListener {
                val intent = Intent(this@SettingsActivity, ChangeUsernameActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun changeTheme() {
        switch = findViewById(R.id.theme_dark)
        sharedPreferences = getSharedPreferences("dark", 0)
        val booleanValue: Boolean = sharedPreferences!!.getBoolean("dark_mode", true)
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switch?.isChecked = true
        }
        switch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switch?.isChecked = true
                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putBoolean("dark_mode", true)
                editor.apply()
                Toast.makeText(this@SettingsActivity, "Dark Mode Aktif", Toast.LENGTH_SHORT).show()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switch?.isChecked = false
                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putBoolean("dark_mode", false)
                editor.apply()
                Toast.makeText(this@SettingsActivity, "Dark Mode Dimatikan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logOut() {
        binding?.btnLogout?.setOnClickListener {
            auth.signOut()
            Toast.makeText(applicationContext, "Berhasil keluar akun", Toast.LENGTH_SHORT).show()

            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}