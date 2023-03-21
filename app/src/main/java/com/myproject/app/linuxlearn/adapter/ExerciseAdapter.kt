package com.myproject.app.linuxlearn.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.app.linuxlearn.data.model.ExerciseModel
import com.myproject.app.linuxlearn.databinding.ListExerciseBinding
import com.myproject.app.linuxlearn.ui.DragAndDropDetailActivity
import com.myproject.app.linuxlearn.ui.ExerciseTypesActivity
import com.myproject.app.linuxlearn.ui.MultiChoiceDetailActivity
import com.myproject.app.linuxlearn.ui.MultipleChoiceDetailActivity

class ExerciseAdapter(var c: Context, private val listExercise: List<ExerciseModel>) : RecyclerView.Adapter<ExerciseAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ListExerciseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ListExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val exercise = listExercise[position]

        holder.binding.apply {
            tvTitle.text = exercise.name
            tvDesc.text = exercise.description
            tvLabelQuestion.text = exercise.questionLabel
            tvLabelSubject.text = exercise.subjectLabel

            holder.itemView.setOnClickListener {
//                val intent = Intent(c, MultipleChoiceDetailActivity::class.java)
                val intent = Intent(c, ExerciseTypesActivity::class.java)
//                intent.putExtra("id", exercise.id)
                intent.putExtra(ExerciseTypesActivity.GET_ID, exercise.id)
                c.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listExercise.size
}