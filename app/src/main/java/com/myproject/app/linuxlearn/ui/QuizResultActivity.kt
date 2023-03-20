package com.myproject.app.linuxlearn.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.myproject.app.linuxlearn.adapter.ExerciseAdapter
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.databinding.ActivityQuizResultBinding

class QuizResultActivity : AppCompatActivity() {
    private var _binding: ActivityQuizResultBinding? = null
    private val binding get() = _binding
    private var exercise: ExerciseModel? = null
    private lateinit var database: DatabaseReference
    private lateinit var exerciseArrayList : ArrayList<ExerciseModel>
    private var score: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getScore()
        getNumberOfExercises()
       // getNumberOfDragAndDropExercises()
        binding?.btnFinish?.setOnClickListener {
            val intent = Intent(this@QuizResultActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getScore() {
        score = intent.extras?.getInt("score")
        binding?.apply {
            tvScoreResult.text = score.toString()
        }
    }

    private fun getNumberOfExercises() {
        database = FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("exercises")
        exerciseArrayList = arrayListOf()

        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val exerciseId = intent.extras?.getString("id")
                    for (exerciseSnapshot in snapshot.child(exerciseId.toString()).child("questions").children) {
                        exercise = exerciseSnapshot.getValue(ExerciseModel::class.java)
                        exercise?.id = exerciseSnapshot.key.toString()

                        if (exerciseSnapshot.key == exerciseId) {
                            binding?.apply {
                                tvName.text = exercise?.name
                            }
                        }

                        exerciseArrayList.add(exercise!!)
                        ExerciseAdapter(applicationContext, exerciseArrayList)
                        binding?.tvNumberExercise?.text = "/" + exerciseArrayList.size.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getNumberOfDragAndDropExercises() {
        database = FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("exercises")
        exerciseArrayList = arrayListOf()

        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val exerciseId = intent.extras?.getString("id")
                    for (exerciseSnapshot in snapshot.child(exerciseId.toString()).child("questionDnd").children) {
                        exercise = exerciseSnapshot.getValue(ExerciseModel::class.java)
                        exercise?.id = exerciseSnapshot.key.toString()

                        if (exerciseSnapshot.key == exerciseId) {
                            binding?.apply {
                                tvName.text = exercise?.name
                            }
                        }

                        exerciseArrayList.add(exercise!!)
                        ExerciseAdapter(applicationContext, exerciseArrayList)
                        binding?.tvNumberExercise?.text = "/" + exerciseArrayList.size.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}