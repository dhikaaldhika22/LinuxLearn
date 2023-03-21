package com.myproject.app.linuxlearn.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myproject.app.linuxlearn.databinding.ActivityQuizResultBinding

class QuizResultActivity : AppCompatActivity() {
    private var _binding: ActivityQuizResultBinding? = null
    private val binding get() = _binding
    private var score: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getScore()

        binding?.btnFinish?.setOnClickListener {
            val intent = Intent(this@QuizResultActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        getNumberOfExercises()
    }

    private fun getScore() {
        score = intent.extras?.getInt("score")
        binding?.apply {
            tvScoreResult.text = score.toString()
        }
    }

    private fun getNumberOfExercises() {
        val multipleChoiceSize = intent.getStringExtra(MULTIPLE_CHOICE)?.toInt() ?: 0
        val dragDropSize = intent.getStringExtra(DRAG_DROP)?.toInt() ?: 0
        val multiAnswerSize = intent.getStringExtra(MULTI_CHOICE)?.toInt() ?: 0

        binding?.apply {
            when {
                dragDropSize > 0 -> tvNumberExercise.text = "/$dragDropSize"
                multiAnswerSize > 0 -> tvNumberExercise.text = "/$multiAnswerSize"
                multipleChoiceSize > 0 -> tvNumberExercise.text = "/$multipleChoiceSize"
                else -> tvNumberExercise.text = "/0"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    companion object {
        const val MULTI_CHOICE = "multi_choice"
        const val MULTIPLE_CHOICE = "multiple_choice"
        const val DRAG_DROP = "drag_drop"
    }
}