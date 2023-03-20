package com.myproject.app.linuxlearn.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.children
import com.google.firebase.database.*
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.adapter.ExerciseAdapter
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.databinding.ActivityDragAndDropDetailBinding

class DragAndDropDetailActivity : AppCompatActivity() {
    private var _binding: ActivityDragAndDropDetailBinding? = null
    private val binding get() = _binding
    private lateinit var database: DatabaseReference
    private lateinit var exerciseArrayList: ArrayList<ExerciseModel>
    private var exerciseId: String? = null
    private var correctAnswerId: Int = 0
    private var selectedOption = 0
    private var currentQuestionPosition = 0
    private var exercise: ExerciseModel? = null
    private var score = 0
    private var initialParents: MutableMap<View, ViewGroup> = mutableMapOf()
    private var dragItem: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDragAndDropDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setToolbar(getString(R.string.exercise))
        getDetailExercise()

        binding?.apply {
            initialParents[tvOption1] = tvOption1.parent as ViewGroup
            initialParents[tvOption2] = tvOption2.parent as ViewGroup
            initialParents[tvOption3] = tvOption3.parent as ViewGroup
            initialParents[tvOption4] = tvOption4.parent as ViewGroup
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

    private fun getDetailExercise() {
        database =
            FirebaseDatabase.getInstance("https://linux-learn-6bdc2-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("exercises")
        exerciseArrayList = arrayListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    exerciseId = intent.getStringExtra("id")
                    for (dragExerciseSnapshot in snapshot.child(exerciseId.toString())
                        .child("questionDnd").children) {
                        exercise = dragExerciseSnapshot.getValue(ExerciseModel::class.java)
                        exercise?.id = dragExerciseSnapshot.key.toString()

                        if (dragExerciseSnapshot.key == "1") {
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
                        ExerciseAdapter(applicationContext, exerciseArrayList)
                        binding?.tvTotalQuestion?.text = "/" + exerciseArrayList.size.toString()
                        val size = exerciseArrayList.size

                        binding?.apply {
                            resetOptions()
                            buttonAction()
                            tvNext.setOnClickListener {
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
                                    tvCurrentQuestion.text =
                                        "Pertanyaan ${currentQuestionPosition + 1}"
                                    if (currentQuestionPosition == size - 1) {
                                        setIndicator()
                                        finishQuiz()
                                    }

                                    for (child in blankBox.children) {
                                        blankBox.removeView(child)
                                        resetOptions()
                                        when (child.id) {
                                            tvOption1.id -> {
                                                ll.addView(child, 0)
                                            }
                                            tvOption2.id -> {
                                                ll.addView(child, 1)
                                            }
                                            tvOption3.id -> {
                                                ll.addView(child, 2)
                                            }
                                            else -> {
                                                ll.addView(child, 3)
                                            }
                                        }
                                    }

                                    Toast.makeText(
                                        this@DragAndDropDetailActivity,
                                        "Skor anda : $score",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@DragAndDropDetailActivity,
                                        "Pilih jawaban terlebih dahulu!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun buttonAction() {
        binding?.apply {
            tvOption1.setOnLongClickListener { view ->
                selectedOption = 1
                selectedOptions(tvOption1)
                val data = ClipData.newPlainText("", "")
                val myDragShadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, myDragShadowBuilder, null, 0)
                dragItem = tvOption1.id
                true
            }


            blankBox.setOnDragListener { view, dragEvent ->
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        true
                    }
                    DragEvent.ACTION_DRAG_ENTERED -> {
//                        when (dragItem) {
//                            tvOption1.id -> {
//                                blankBox.removeView(tvOption2)
//                                blankBox.removeView(tvOption3)
//                                blankBox.removeView(tvOption4)
//                                ll.addView(tvOption2)
//                                ll.addView(tvOption3)
//                                ll.addView(tvOption4)
//                                blankBox.addView(tvOption1)
//                            }
//                        }
                        true
                    }
                    DragEvent.ACTION_DRAG_EXITED -> {
                        true
                    }
                    DragEvent.ACTION_DROP -> {
                        blankBox.removeAllViews()
                        ll.removeAllViews()
                        ll.addView(tvOption1)
                        ll.addView(tvOption2)
                        ll.addView(tvOption3)
                        ll.addView(tvOption4)
                        when (dragItem) {
                            tvOption1.id -> {
                                ll.removeView(tvOption1)
                                blankBox.addView(tvOption1)
                            }

                            tvOption2.id -> {
                                ll.removeView(tvOption2)
                                blankBox.addView(tvOption2)
                            }

                            tvOption3.id -> {
                                ll.removeView(tvOption3)
                                blankBox.addView(tvOption3)
                            }

                            tvOption4.id -> {
                                ll.removeView(tvOption4)
                                blankBox.addView(tvOption4)
                            }
                            else -> {
                            }
                        }
                        true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

//            rlOption1.setOnDragListener { view, dragEvent ->
//                when (dragEvent.action) {
//                    DragEvent.ACTION_DRAG_STARTED -> {
//                        if (dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//                            (view as? RelativeLayout)?.setBackgroundColor(R.color.secondary)
//                            view.invalidate()
//                            true
//                        } else {
//                            false
//                        }
//                    }
//                    DragEvent.ACTION_DRAG_ENTERED -> {
//                        (view as? RelativeLayout)?.setBackgroundColor(R.color.secondary)
//                        view.invalidate()
//                        true
//                    }
//                    DragEvent.ACTION_DRAG_LOCATION -> {
//                        true
//                    }
//                    DragEvent.ACTION_DRAG_EXITED -> {
//                        (view as? RelativeLayout)?.setBackgroundColor(R.color.secondary)
//                        view.invalidate()
//                        true
//                    }
//                    DragEvent.ACTION_DROP -> {
//                        val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
//                        val dragData = item.text
//                        Toast.makeText(this@DragAndDropDetailActivity, "Data yang di drag adalah $dragData", Toast.LENGTH_SHORT).show()
//                        (view as? RelativeLayout)?.setBackgroundColor(R.color.secondary)
//                        view.invalidate()
//                        true
//                    }
//                    DragEvent.ACTION_DRAG_ENDED -> {
//                        (view as? RelativeLayout)?.setBackgroundColor(R.color.primer)
//                        view.invalidate()
//                        when(dragEvent.result) {
//                            true ->
//                                Toast.makeText(this@DragAndDropDetailActivity, "Handled", Toast.LENGTH_LONG)
//                            else ->
//                                Toast.makeText(this@DragAndDropDetailActivity, "Not Handled", Toast.LENGTH_LONG)
//                        }.show()
//
//                        true
//                    }
//                    else -> {
//                        Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.")
//                        false
//                    }
//                }
//            }
            tvOption2.setOnLongClickListener { view ->
                selectedOption = 2
                selectedOptions(tvOption2)
                val data = ClipData.newPlainText("", "")
                val myDragShadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, myDragShadowBuilder, null, 0)
                dragItem = tvOption2.id
                true
            }

            tvOption3.setOnLongClickListener { view ->
                selectedOption = 3
                selectedOptions(tvOption3)
                val data = ClipData.newPlainText("", "")
                val myDragShadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, myDragShadowBuilder, null, 0)
                dragItem = tvOption3.id
                true
            }

            tvOption4.setOnLongClickListener { view ->
                selectedOption = 4
                selectedOptions(tvOption4)
                val data = ClipData.newPlainText("", "")
                val myDragShadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, myDragShadowBuilder, null, 0)
                dragItem = tvOption4.id
                true
            }
        }
    }

    private fun finishQuiz() {
        binding?.apply {
            btnSubmit.setOnClickListener {
                if (selectedOption == correctAnswerId) {
                    score++
                }
                val intent = Intent(this@DragAndDropDetailActivity, QuizResultActivity::class.java)
                intent.putExtra("score", score)
                intent.putExtra("id", exerciseId)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun selectedOptions(selectOption: TextView) {
        resetOptions()

        selectOption.setBackgroundResource(R.drawable.bg_selected_option)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun resetOptions() {
        binding?.apply {
            tvOption1.cancelDragAndDrop()
            tvOption2.cancelDragAndDrop()
            tvOption3.cancelDragAndDrop()
            tvOption4.cancelDragAndDrop()

            tvOption1.setBackgroundResource(R.drawable.round_back_white)
            tvOption2.setBackgroundResource(R.drawable.round_back_white)
            tvOption3.setBackgroundResource(R.drawable.round_back_white)
            tvOption4.setBackgroundResource(R.drawable.round_back_white)
        }
    }

    private fun setIndicator() {
        binding?.apply {
            btnSubmit.visibility = View.VISIBLE
            tvNext.visibility = View.GONE
        }
    }
}