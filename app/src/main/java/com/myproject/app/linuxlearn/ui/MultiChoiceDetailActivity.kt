package com.myproject.app.linuxlearn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.widget.Toast
import androidx.core.os.HandlerCompat
import com.google.firebase.database.*
import com.myproject.app.linuxlearn.Constant
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.adapter.ExerciseAdapter
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.databinding.ActivityMultiChoiceBinding

class MultiChoiceDetailActivity : AppCompatActivity() {
    private var _binding: ActivityMultiChoiceBinding? = null
    private val binding get() = _binding
    private lateinit var database: DatabaseReference
    private lateinit var exerciseArrayList : ArrayList<ExerciseModel>
    private var exercise: ExerciseModel? = null
    private var score = 0
    private var answer = ""
    private var correctAnswers: String = ""
    private var exerciseId: String? = null
    private var currentQuestionPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMultiChoiceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setToolbar(getString(R.string.exercise))
        getDetailExercise()
        binding?.apply {
            cbOption1.setOnCheckedChangeListener {_,_,-> initCheckbox()}
            cbOption2.setOnCheckedChangeListener {_,_,-> initCheckbox()}
            cbOption3.setOnCheckedChangeListener {_,_,-> initCheckbox()}
            cbOption4.setOnCheckedChangeListener {_,_,-> initCheckbox()}
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

        database.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val handler = HandlerCompat.createAsync(mainLooper)
                    handler.postDelayed({
                        exerciseId = intent.getStringExtra(GET_ID)
                        for (multiSnapshot in snapshot.child(exerciseId.toString())
                            .child(Constant.multiAnswerQuestionEndpoint).children) {
                            exercise = multiSnapshot.getValue(ExerciseModel::class.java)
                            exercise?.id = multiSnapshot.key.toString()

                            if (multiSnapshot.key == "1") {
                                binding?.apply {
                                    tvName.text = exercise?.name
                                    tvQuestion.text = exercise?.question
                                    cbOption1.text = exercise?.optionA
                                    cbOption2.text = exercise?.optionB
                                    cbOption3.text = exercise?.optionC
                                    cbOption4.text = exercise?.optionD

                                    shimmerTotalQuestion.visibility = View.GONE
                                    shimmerDisplayQuestion.visibility = View.GONE
                                    shimmerExerciseName.visibility = View.GONE
                                    shimmerBox.visibility = View.GONE

                                    cbOption1.visibility = View.VISIBLE
                                    cbOption2.visibility = View.VISIBLE
                                    cbOption3.visibility = View.VISIBLE
                                    cbOption4.visibility = View.VISIBLE

                                    tvNext.visibility = View.VISIBLE
                                }
                            }

                            exerciseArrayList.add(exercise!!)
                            ExerciseAdapter(applicationContext, exerciseArrayList)
                            binding?.tvTotalQuestion?.text = "/" + exerciseArrayList.size
                            val size = exerciseArrayList.size

                            binding?.apply {
                                tvNext.setOnClickListener {
                                    resetOptions()
                                    if (answer != "") {
                                        correctAnswers = exercise?.answerMulti ?: ""
                                        val selectedAnswers = answer
                                        val answerList =
                                            selectedAnswers.split(",").map { it.toInt() }
                                        val sortedList = answerList.sorted()
                                        val sortedAnswer = sortedList.joinToString(",")
                                        val correctAnswerList = correctAnswers.split(", ")
                                        for (answer in correctAnswerList) {
                                            if (sortedAnswer.contains(answer)) {
                                                score++
                                            }
                                        }
                                        answer = ""
                                        currentQuestionPosition++
                                        exercise = exerciseArrayList[currentQuestionPosition]
                                        binding?.apply {
                                            tvName.text = exercise?.name
                                            tvQuestion.text = exercise?.question
                                            cbOption1.text = exercise?.optionA
                                            cbOption2.text = exercise?.optionB
                                            cbOption3.text = exercise?.optionC
                                            cbOption4.text = exercise?.optionD
                                        }

                                        tvCurrentQuestion.text =
                                            "Pertanyaan ${currentQuestionPosition + 1}"
                                        if (currentQuestionPosition == size - 1) {
                                            setIndicator()
//                                        finishQuiz()
                                            btnSubmit.setOnClickListener {
                                                for (answer in correctAnswerList) {
                                                    if (sortedAnswer.contains(answer)) {
                                                        score++
                                                    }
                                                }
                                                val intent = Intent(
                                                    this@MultiChoiceDetailActivity,
                                                    QuizResultActivity::class.java
                                                )
                                                intent.putExtra("score", score)
                                                intent.putExtra("id", exerciseId)
                                                intent.putExtra(
                                                    QuizResultActivity.MULTI_CHOICE,
                                                    size.toString()
                                                )
                                                Log.d(
                                                    "Extra",
                                                    "exerciseArrayList multisize: ${size}"
                                                )
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                        Toast.makeText(
                                            this@MultiChoiceDetailActivity,
                                            "Skor anda : $score",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@MultiChoiceDetailActivity,
                                            "Pilih jawaban terlebih dahulu!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }, 2000)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initCheckbox() {
        binding?.apply {
            if (cbOption1.isChecked && !answer.contains("1")) {
                answer += "1, "
            }
            if (cbOption2.isChecked && !answer.contains("2")) {
                answer += "2, "
            }
            if (cbOption3.isChecked && !answer.contains("3")) {
                answer += "3, "
            }
            if (cbOption4.isChecked && !answer.contains("4")) {
                answer += "4, "
            }
            answer = answer.trimEnd(',').trim()
        }
    }

    private fun finishQuiz() {
        binding?.apply {
            btnSubmit.setOnClickListener {
                initCheckbox()
//                val selectedAnswers = answer
//                val answerList = selectedAnswers.split(",").mapNotNull { it.toIntOrNull() }
//                val sortedList = answerList.sorted()
//                val sortedAnswer = sortedList.joinToString(",")
//                val correctAnswerList = correctAnswers.split(", ")
//                for (answer in correctAnswerList) {
//                    if (sortedAnswer.contains(answer)) {
//                        score++
//                    }
//                }
//                val intent = Intent(this@MultiChoiceDetailActivity, QuizResultActivity::class.java)
//                intent.putExtra("score", score)
//                intent.putExtra("id", exerciseId)
//                startActivity(intent)
//                finish()
                val selectedAnswers = answer
                val answerList = selectedAnswers.split(",").map { it.toInt() }
                val sortedList = answerList.sorted()
                val sortedAnswer = sortedList.joinToString(",")
                val correctAnswerList = correctAnswers.split(", ")
                for (answer in correctAnswerList) {
                    if (sortedAnswer.contains(answer)) {
                        score++
                    }
                }
                val intent = Intent(this@MultiChoiceDetailActivity, QuizResultActivity::class.java)
                intent.putExtra("score", score)
                intent.putExtra("id", exerciseId)
                intent.putExtra(QuizResultActivity.MULTI_CHOICE, exerciseArrayList.size.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    private fun resetOptions() {
        binding?.apply {
            cbOption1.setBackgroundResource(R.drawable.round_back_white)
            cbOption2.setBackgroundResource(R.drawable.round_back_white)
            cbOption3.setBackgroundResource(R.drawable.round_back_white)
            cbOption4.setBackgroundResource(R.drawable.round_back_white)

            val checkBoxes = listOf(cbOption1, cbOption2, cbOption3, cbOption4)
            checkBoxes.forEach { it.isChecked = false }
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