@file:Suppress("DEPRECATION")

package com.myproject.app.linuxlearn.ui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.myproject.app.linuxlearn.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var imageUri: Uri
    private var myUri: String = ""
    private var uploadTask: StorageTask<*>? = null
    private val storageReferencePic: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth
        initAction()
        getUserData()

        return binding?.root
    }

    private fun initAction() {
        binding?.apply {
            btnSettings.setOnClickListener {
                val intent = Intent(requireContext(), SettingsActivity::class.java)
                startActivity(intent)
            }
            ivProfileImage.setOnClickListener {
                CropImage.activity().setAspectRatio(1,1).start(requireContext() as Activity)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            imageUri = result.uri
            val imgBitmap = data.extras?.get("data") as Bitmap
            uploadProfileImage(imgBitmap)
            binding?.ivProfileImage?.setImageURI(imageUri)
        } else {
            Toast.makeText(requireContext(), "Terdapat kesalahan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadProfileImage(imgBitmap: Bitmap) {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("set your  profile")
        progressDialog.setMessage("Please wait, while we are setting your data")
        progressDialog.show()
        if (imageUri != null) {
            val baOs = ByteArrayOutputStream()
            // val fileReference: StorageReference = storageReferencePic!!.child(auth.currentUser?.uid+".jpg")
            val ref = FirebaseStorage.getInstance("gs://linux-learn-6bdc2.appspot.com").reference.child("img/${auth.currentUser?.uid}")
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baOs)
            val img = baOs.toByteArray()
            ref.putBytes(img).addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnCompleteListener {
                        it.result?.let {
                            imageUri = it
                            binding?.ivProfileImage?.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
//            uploadTask = fileReference.putFile(imageUri)

        }
    }


    private fun getUserData() {
        val user = auth.currentUser
        database = FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (user?.let { snapshot.hasChild(it.uid) } == true) {
                    Handler().postDelayed({
                        binding?.apply {
                            tvEmail.text = snapshot.child(user.uid).child("email").getValue(String::class.java)
                            tvUsername.text = snapshot.child(user.uid).child("username").getValue(String::class.java)
                            binding?.tvUsernameBox?.text = snapshot.child(user.uid).child("username").getValue(String::class.java)
                            tvEmailBox.text = snapshot.child(user.uid).child("email").getValue(String::class.java)
                            //tvUsername.text = user.displayName
                            //tvUsernameBox.text = user.displayName
                            shimmerDisplayName.visibility = View.GONE
                            shimmerEmail.visibility = View.GONE
                            shimmerUsernameBox.visibility = View.GONE
                            tvUsernameBox.visibility = View.VISIBLE
                            shimmerEmailBox.visibility = View.GONE
                            tvEmailBox.visibility = View.VISIBLE
                        }
                    }, 2000)
                    if (snapshot.exists() && snapshot.childrenCount > 0) {
                        if (snapshot.hasChild("photo")) {
                            val image = snapshot.child("image").value.toString()
                            Picasso.get().load(image).into(binding?.ivProfileImage)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}