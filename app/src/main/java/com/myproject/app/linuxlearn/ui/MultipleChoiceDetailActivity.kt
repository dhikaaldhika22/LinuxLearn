package com.myproject.app.linuxlearn.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import com.google.firebase.database.*
import com.myproject.app.linuxlearn.Constant
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.adapter.ExerciseAdapter
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.databinding.ActivityMultiplechoiceDetailBinding

class MultipleChoiceDetailActivity : AppCompatActivity() {
    private var _binding: ActivityMultiplechoiceDetailBinding? = null
    private val binding get() = _binding
    private lateinit var database: DatabaseReference
    private lateinit var exerciseArrayList : ArrayList<ExerciseModel>

    private var exerciseId: String? = null
    private var correctAnswerId: Int = 0
    private var selectedOption = 0
    private var currentQuestionPosition = 0
    private var exercise: ExerciseModel? = null
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMultiplechoiceDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setToolbar(getString(R.string.exercise))
        multipleButtonAction()
        getDetailExercise()
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun setIndicator() {
        binding?.apply {
            btnSubmit.visibility = View.VISIBLE
            tvNext.visibility = View.GONE
        }
    }

    private fun getDetailExercise() {
        database = FirebaseDatabase.getInstance(Constant.base_url)
            .getReference(Constant.exerciseEndpoint)
        exerciseArrayList = arrayListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val handler = HandlerCompat.createAsync(mainLooper)
                    handler.postDelayed({
//                        exerciseId = intent.getStringExtra("id")
                        exerciseId = intent.getStringExtra(GET_ID)
                        Log.d("asu get", "onData: $exerciseId")
                        for (exerciseSnapshot in snapshot.child(exerciseId.toString()).child(Constant.multipleChoiceQuestionEndpoint).children) {
                            exercise = exerciseSnapshot.getValue(ExerciseModel::class.java)
                            exercise?.id = exerciseSnapshot.key.toString()

                            if (exerciseSnapshot.key == exerciseId.toString()) {
                                binding?.apply {
                                    tvName.text = exercise?.name
                                    tvQuestion.text = exercise?.question
                                    tvOption1.text = exercise?.optionA
                                    tvOption2.text = exercise?.optionB
                                    tvOption3.text = exercise?.optionC
                                    tvOption4.text = exercise?.optionD
                                }
                            }
                            exerciseArrayList.add(exercise!!)

                            binding?.apply {
                                shimmerTotalQuestion.visibility = View.GONE
                                shimmerDisplayQuestion.visibility = View.GONE
                                shimmerExerciseName.visibility = View.GONE
                                shimmerQuestionBox.visibility = View.GONE

                                rlOption1.visibility = View.VISIBLE
                                rlOption2.visibility = View.VISIBLE
                                rlOption3.visibility = View.VISIBLE
                                rlOption4.visibility = View.VISIBLE

                                tvNext.visibility = View.VISIBLE
                            }

                            ExerciseAdapter(applicationContext, exerciseArrayList)
                            binding?.tvTotalQuestion?.text = "/" + exerciseArrayList.size.toString()
                            val size = exerciseArrayList.size

                            binding?.apply {
                                tvNext.setOnClickListener {
                                    resetOptions()
                                    if (selectedOption != 0) {
                                        correctAnswerId = exercise?.answer ?: 0
                                        if (selectedOption == correctAnswerId) {
                                            score++
                                        }
                                        selectedOption = 0
                                        currentQuestionPosition++
                                        exercise = exerciseArrayList[currentQuestionPosition]
                                        binding?.apply {
                                            tvName.text = exercise?.name
                                            tvQuestion.text = exercise?.question
                                            tvOption1.text = exercise?.optionA
                                            tvOption2.text = exercise?.optionB
                                            tvOption3.text = exercise?.optionC
                                            tvOption4.text = exercise?.optionD
                                        }

                                        correctAnswerId = exercise?.answer ?: 0
                                        if (selectedOption == correctAnswerId) {
                                            score++
                                        }
                                        tvCurrentQuestion.text = "Pertanyaan ${currentQuestionPosition + 1}"
                                        if (currentQuestionPosition == size-1) {
                                            setIndicator()
//                                            finishQuiz
                                            btnSubmit.setOnClickListener {
                                                if (selectedOption == correctAnswerId) {
                                                    score++
                                                }
                                                val intent = Intent(this@MultipleChoiceDetailActivity, QuizResultActivity::class.java)
                                                intent.putExtra("score", score)
                                                intent.putExtra("id", exerciseId)
                                                intent.putExtra(QuizResultActivity.MULTIPLE_CHOICE, size.toString())
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                        Toast.makeText(this@MultipleChoiceDetailActivity, "Skor anda : $score", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@MultipleChoiceDetailActivity, "Pilih jawaban terlebih dahulu!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }, 2000)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MultipleChoiceDetailActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun multipleButtonAction() {
        binding?.apply {
            rlOption1.setOnClickListener {
                selectedOption = 1
                multipleSelectedOptions(rlOption1, ivSelect1)
            }

            rlOption2.setOnClickListener {
                selectedOption = 2
                multipleSelectedOptions(rlOption2, ivSelect2)
            }

            rlOption3.setOnClickListener {
                selectedOption = 3
                multipleSelectedOptions(rlOption3, ivSelect3)
            }

            rlOption4.setOnClickListener {
                selectedOption = 4
                multipleSelectedOptions(rlOption4, ivSelect4)
            }
        }
    }

    private fun multipleSelectedOptions(selectOption: RelativeLayout, selectedOptionIcon: ImageView) {
        resetOptions()

        selectedOptionIcon.setImageResource(R.drawable.ic_check)
        selectOption.setBackgroundResource(R.drawable.bg_selected_option)
    }

    private fun resetOptions() {
        binding?.apply {
            rlOption1.setBackgroundResource(R.drawable.round_back_white)
            rlOption2.setBackgroundResource(R.drawable.round_back_white)
            rlOption3.setBackgroundResource(R.drawable.round_back_white)
            rlOption4.setBackgroundResource(R.drawable.round_back_white)


            ivSelect1.setImageResource(R.drawable.round_back_white)
            ivSelect2.setImageResource(R.drawable.round_back_white)
            ivSelect3.setImageResource(R.drawable.round_back_white)
            ivSelect4.setImageResource(R.drawable.round_back_white)
        }
    }

    private fun finishQuiz() {
        binding?.apply {
            btnSubmit.setOnClickListener {
                if (selectedOption == correctAnswerId) {
                    score++
                }
                val intent = Intent(this@MultipleChoiceDetailActivity, QuizResultActivity::class.java)
                intent.putExtra("score", score)
                intent.putExtra("id", exerciseId)
                intent.putExtra(QuizResultActivity.MULTIPLE_CHOICE, exerciseArrayList.size.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return super.onSupportNavigateUp()
    }

    companion object {
        const val GET_ID = "get_id"
    }
}