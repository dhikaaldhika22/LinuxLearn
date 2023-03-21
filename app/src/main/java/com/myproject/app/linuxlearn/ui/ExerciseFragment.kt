package com.myproject.app.linuxlearn.ui

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.myproject.app.linuxlearn.Constant
import com.myproject.app.linuxlearn.adapter.ExerciseAdapter
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding
    private lateinit var database: DatabaseReference
    private lateinit var exerciseArrayList : ArrayList<ExerciseModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseBinding.inflate(layoutInflater, container, false)

        getExercise()
        return binding?.root
    }

    private fun getExercise() {
        database = FirebaseDatabase.getInstance(Constant.base_url)
            .getReference(Constant.exerciseEndpoint)
        exerciseArrayList = arrayListOf()
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val handler = HandlerCompat.createAsync(Looper.getMainLooper())
                    handler.postDelayed({
                        for (exerciseSnapshot in snapshot.children) {
                            val exercise = exerciseSnapshot.getValue(ExerciseModel::class.java)
                            exercise?.id = exerciseSnapshot.key.toString()
                            exerciseArrayList.add(exercise!!)

                            binding?.progressbar?.visibility = View.GONE
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