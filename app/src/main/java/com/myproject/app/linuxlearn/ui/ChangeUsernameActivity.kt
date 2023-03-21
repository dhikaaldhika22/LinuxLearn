package com.myproject.app.linuxlearn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.myproject.app.linuxlearn.Constant
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.data.model.UserModel
import com.myproject.app.linuxlearn.databinding.ActivityChangeUsernameBinding

class ChangeUsernameActivity : AppCompatActivity() {
    private var _binding: ActivityChangeUsernameBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChangeUsernameBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth
        getUsername()

        binding?.btnSaveChanges?.setOnClickListener {
            updateData()
        }
        setToolbar(getString(R.string.change_account))
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun getUsername() {
        val user = auth.currentUser
        database = FirebaseDatabase.getInstance(Constant.base_url).getReference(Constant.usersEndpoint)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (user?.let { snapshot.hasChild(it.uid) } == true) {
                     binding?.tilUsername?.setText(snapshot.child(user.uid).child("username").getValue(String::class.java))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    private fun updateData() {
        binding?.apply {
            val user = auth.currentUser
            val name = tilUsername.text.toString().trim()

            if (TextUtils.isEmpty(name)) {
                etUsername.error = "Harus diisi!"
                etUsername.requestFocus()
            } else {
//                UserProfileChangeRequest.Builder()
//                    .setDisplayName(name)
//                    .build().also {
//                        user?.updateProfile(it)?.addOnCompleteListener { it ->
//                            if (it.isSuccessful) {
//                                val intent = Intent(this@ChangeUsernameActivity, SettingsActivity::class.java)
//                                startActivity(intent)
//                                finish()
//                                Toast.makeText(this@ChangeUsernameActivity,  "Nama Pengguna Berhasil diupdate!", Toast.LENGTH_SHORT).show()
//                            } else {
//                                Toast.makeText(this@ChangeUsernameActivity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
                val userRef = database.child("${user?.uid}")
                val userMap = mapOf(
                    "username" to name
                )
                userRef.updateChildren(userMap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this@ChangeUsernameActivity, SettingsActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@ChangeUsernameActivity,  "Nama Pengguna Berhasil diupdate!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ChangeUsernameActivity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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