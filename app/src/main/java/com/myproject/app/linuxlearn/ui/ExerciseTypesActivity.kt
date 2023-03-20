package com.myproject.app.linuxlearn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.databinding.ActivityExerciseTypesBinding

class ExerciseTypesActivity : AppCompatActivity() {
    private var _binding: ActivityExerciseTypesBinding? = null
    private val binding get() = _binding
    private lateinit var exerciseArrayList : ArrayList<ExerciseModel>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityExerciseTypesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        val exerciseIntent = intent
//        id = exerciseIntent.getStringExtra("id")
//        id = intent.extras?.getString("id")
        initAction()
        setToolbar(getString(R.string.type_question))
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun initAction() {
        binding?.apply {
            cvMultipleChoice.setOnClickListener {
//                val i = Intent(this@ExerciseTypesActivity, DetailExercisesActivity::class.java)
////                val exerciseIntent = intent
////                val id = exerciseIntent.getStringExtra("id")
//                val exerciseModel = ExerciseModel()
//                intent.putExtra("id", exerciseModel.id)
//                startActivity(i)
                toMultipleChoice()
            }

            cvMultiChoice.setOnClickListener {
                toMultiChoice()
            }
        }
    }

    private fun toMultipleChoice() {
        database = FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("exercises")

        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                        for (exerciseSnapshot in snapshot.children) {
                            val exercise = exerciseSnapshot.getValue(ExerciseModel::class.java)
//                            exercise?.id = exerciseSnapshot.key.toString()
                           // exercise?.id = intent.extras?.getString("id").toString()
                            var id = intent.extras?.getString("id")
                            val i = Intent(this@ExerciseTypesActivity, MultipleChoiceDetailActivity::class.java)
                            intent.extras?.putString(id, exercise?.id)
                            startActivity(i)
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun toMultiChoice() {
        val i = Intent(this@ExerciseTypesActivity, MultiChoiceDetailActivity::class.java)
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return super.onSupportNavigateUp()
    }
}