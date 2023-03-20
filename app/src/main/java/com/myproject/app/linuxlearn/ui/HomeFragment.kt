@file:Suppress("DEPRECATION")

package com.myproject.app.linuxlearn.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.myproject.app.linuxlearn.adapter.ExerciseAdapter
import com.myproject.app.linuxlearn.adapter.SubjectMatterAdapter
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.data.model.SubjectMatterModel
import com.myproject.app.linuxlearn.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var subjectMatterArrayList : ArrayList<SubjectMatterModel>
    private lateinit var exerciseArrayList : ArrayList<ExerciseModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth
        getUsername()
        getSubjectMatter()
        getExercise()
        initAction()

        return binding?.root
    }

    private fun initAction() {
        binding?.apply {
            tvSeeAll.setOnClickListener {
                val i = Intent(requireContext(), SeeAllSubjectMatterActivity::class.java)
                startActivity(i)
            }

            tvSeeAll2.setOnClickListener {
                val i = Intent(requireContext(), SeeAllExercisesActivity::class.java)
                startActivity(i)
            }
        }
    }

//    private fun getProfilePicture() {
//        val userRef = database.child("users/${auth?.uid}")
//        val userMap = mapOf(
//            "photo" to
//        )
//        userRef.updateChildren(userMap)
//    }

    private fun getUsername() {
        val user = auth.currentUser
        database = FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (user?.let { snapshot.hasChild(it.uid) } == true) {
                    Handler().postDelayed({
                        binding?.tvUsername?.text = snapshot.child(user.uid).child("username").getValue(String::class.java)
                        binding?.tvUsername?.visibility = View.VISIBLE
                        binding?.shimmerDisplayName?.visibility = View.GONE
                    }, 2000)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getSubjectMatter() {
        database = FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("subjectmatter")
        subjectMatterArrayList = arrayListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Handler().postDelayed({
                        for (subjectMatterSnapshot in snapshot.children) {
                            val subjectMatter = subjectMatterSnapshot.getValue(SubjectMatterModel::class.java)
                            subjectMatter?.id = subjectMatterSnapshot.key.toString()
                            subjectMatterArrayList.add(subjectMatter!!)

                            binding?.progressbar?.visibility = View.GONE
                            binding?.tvExercise?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                topToBottom = binding!!.rvSubjectMatter.id
                            }

                            binding?.tvSeeAll2?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                                topToBottom = binding!!.rvSubjectMatter.id
                            }
                        }

                        binding?.rvSubjectMatter?.apply {
                            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            adapter = SubjectMatterAdapter(context, subjectMatterArrayList)
                            setHasFixedSize(true)
                        }
                    }, 2000)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getExercise() {
        database = FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("exercises")
        exerciseArrayList = arrayListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Handler().postDelayed({
                        for (exerciseSnapshot in snapshot.children) {
                            val exercise = exerciseSnapshot.getValue(ExerciseModel::class.java)
                            exercise?.id = exerciseSnapshot.key.toString()
                            exerciseArrayList.add(exercise!!)

                            binding?.progressbar2?.visibility = View.GONE
                        }

                        binding?.rvExercise?.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = ExerciseAdapter(context, exerciseArrayList)
                            setHasFixedSize(true)
                        }
                    }, 2000)
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