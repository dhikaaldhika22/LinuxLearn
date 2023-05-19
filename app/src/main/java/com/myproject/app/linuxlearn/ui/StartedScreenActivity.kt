package com.myproject.app.linuxlearn.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.databinding.ActivityStartedScreenBinding

class StartedScreenActivity : AppCompatActivity() {
    private var _binding: ActivityStartedScreenBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStartedScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initAction()
    }

    private fun initAction() {
        binding?.apply {
            val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                layout.setBackgroundResource(R.drawable.startedbackground_night)
            } else {
                layout.setBackgroundResource(R.drawable.started_background)
            }

            btnLogin.setOnClickListener {
                val intent = Intent(this@StartedScreenActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnRegister.setOnClickListener {
                val intent = Intent(this@StartedScreenActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}