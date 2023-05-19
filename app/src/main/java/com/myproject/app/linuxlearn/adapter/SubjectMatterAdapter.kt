package com.myproject.app.linuxlearn.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myproject.app.linuxlearn.data.model.SubjectMatterModel
import com.myproject.app.linuxlearn.databinding.ListSubjectmatterBinding
import com.myproject.app.linuxlearn.ui.DetailSubjectMatterActivity

class SubjectMatterAdapter(var c: Context, private val listSubject: List<SubjectMatterModel>) : RecyclerView.Adapter<SubjectMatterAdapter.ListViewHolder>() {
    class ListViewHolder (var binding: ListSubjectmatterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListSubjectmatterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val subjectMatter = listSubject[position]
        val detailSubjectMatter = listSubject[position]

        holder.binding.apply {
            Glide.with(ivThumbnail.context)
                .load(subjectMatter.imageUrl)
                .centerCrop()
                .into(ivThumbnail)
        tvTitle.text = subjectMatter.name
        tvLabel.text = subjectMatter.subheading
        tvDescription.text = subjectMatter.description

        holder.itemView.setOnClickListener {
            val intent = Intent(c, DetailSubjectMatterActivity::class.java)
            val name = detailSubjectMatter.name
            val subheading = detailSubjectMatter.subheading
            val label = detailSubjectMatter.label
            val label2 = detailSubjectMatter.label2
            intent.putExtra("id", subjectMatter.id)
            intent.putExtra("name", name)
            intent.putExtra("subheading", subheading)
            intent.putExtra("label", label)
            intent.putExtra("label2", label2)
            c.startActivity(intent)
        }
        }
    }

    override fun getItemCount(): Int = listSubject.size
}