package com.myproject.app.linuxlearn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.databinding.ActivityExerciseTypesBinding

class ExerciseTypesActivity : AppCompatActivity() {
    private var _binding: ActivityExerciseTypesBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityExerciseTypesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

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
                toMultipleChoice()
            }

            cvMultiChoice.setOnClickListener {
                toMultiChoice()
            }

            cvDragDrop.setOnClickListener {
                toDragAndDrop()
            }
        }
    }

    private fun toMultipleChoice() {
        val getType = intent.getStringExtra(GET_ID)
        val i = Intent(this@ExerciseTypesActivity, MultipleChoiceDetailActivity::class.java)
        i.putExtra(MultipleChoiceDetailActivity.GET_ID, getType)
        startActivity(i)
    }

    private fun toMultiChoice() {
        val getType = intent.getStringExtra(GET_ID)
        val i = Intent(this@ExerciseTypesActivity, MultiChoiceDetailActivity::class.java)
        i.putExtra(MultiChoiceDetailActivity.GET_ID, getType)
        startActivity(i)
    }

    private fun toDragAndDrop() {
        val getType = intent.getStringExtra(GET_ID)
        val i = Intent(this@ExerciseTypesActivity, DragAndDropDetailActivity::class.java)
        i.putExtra(DragAndDropDetailActivity.GET_ID, getType)
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

    companion object {
        const val GET_ID = "get_id"
    }
}