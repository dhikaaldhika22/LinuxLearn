package com.myproject.app.linuxlearn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.os.HandlerCompat
import com.google.firebase.database.*
import com.myproject.app.linuxlearn.Constant
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.data.model.ContentModel
import com.myproject.app.linuxlearn.databinding.ActivityDetailSubjectMatterBinding

class DetailSubjectMatterActivity : AppCompatActivity() {
    private var _binding: ActivityDetailSubjectMatterBinding? = null
    private val binding get() = _binding
    private lateinit var database: DatabaseReference
    private lateinit var contentArrayList : ArrayList<ContentModel>
    private var currentSubjectMatterPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailSubjectMatterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setToolbar(getString(R.string.subject_matter))
        getDetailSubjectMatter()
        getDetailInfo()
        buttonAction()


    }

    private fun buttonAction() {
        binding?.apply {
            tvNext.setOnClickListener {
                currentSubjectMatterPosition++
                val content = contentArrayList[currentSubjectMatterPosition]
                tvContent.text = content.section
                if (tvContent.text.contains("_n")) {
                    val newContent: String = (tvContent.text as String?)!!.replace("_n", "\n\n")
                    tvContent.text = newContent
                }
                if (tvContent.text.contains(("*l"))) {
                    val numberList = (tvContent.text as String).replace("*l", "\n")
                    tvContent.text = numberList
                }
                setIndicator()
            }

            tvBack.setOnClickListener {
                currentSubjectMatterPosition--
                val content = contentArrayList[currentSubjectMatterPosition]
                tvContent.text = content.section
                if (tvContent.text.contains("_n")) {
                    val newContent: String = (tvContent.text as String?)!!.replace("_n", "\n\n")
                    tvContent.text = newContent
                }
                if (tvContent.text.contains(("*l"))) {
                    val numberList = (tvContent.text as String).replace("*l", "\n")
                    tvContent.text = numberList
                }
                setIndicator()
            }

            btnSubjectMatter.setOnClickListener {
                val i = Intent(this@DetailSubjectMatterActivity, SeeAllSubjectMatterActivity::class.java)
                startActivity(i)
            }

            btnStartExercise.setOnClickListener {
                val i = Intent(this@DetailSubjectMatterActivity, SeeAllExercisesActivity::class.java)
                startActivity(i)
            }
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

    private fun getDetailInfo() {
        val subjectMatterIntent = intent
        val subjectMatterName = subjectMatterIntent.getStringExtra("name")
        val subjectMatterLabel = subjectMatterIntent.getStringExtra("label")

        binding?.apply {
            tvName.text = subjectMatterName
            tvLabel1.text = subjectMatterLabel
        }
    }

    private fun videoPlayer() {

    }

    private fun getDetailSubjectMatter() {
        database = FirebaseDatabase.getInstance(Constant.base_url)
            .getReference(Constant.subjectMatterEndpoint)
        contentArrayList = arrayListOf()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val handler = HandlerCompat.createAsync(mainLooper)
                    handler.postDelayed({
                        val subjectMatterId = intent.extras?.getString("id")
                        for (contentSnapshot in snapshot.child(subjectMatterId.toString()).child(Constant.contentEndpoint).children) {
                            val content =
                                contentSnapshot.getValue(ContentModel::class.java)
                            if (contentSnapshot.key == "1") {
                                binding?.apply {
                                    tvContent.text = content?.section
                                    if (tvContent.text.contains("_n")) {
                                        val newContent: String = (tvContent.text as String?)!!.replace("_n", "\n\n")
                                        tvContent.text = newContent
                                    }
                                    if (tvContent.text.contains(("*l"))) {
                                        val numberList = (tvContent.text as String).replace("*l", "\n")
                                        tvContent.text = numberList
                                    }
                                }
                            }
                            contentArrayList.add(content!!)
                        }
                    }, 2000)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailSubjectMatterActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setIndicator() {
        when(currentSubjectMatterPosition) {
            0 -> {
                binding?.apply {
                    nav1.setBackgroundResource(R.drawable.bg_nav_active)
                    nav2.setBackgroundResource(R.drawable.bg_nav_nonactive)
                    nav3.setBackgroundResource(R.drawable.bg_nav_nonactive)
                    tvBack.visibility = View.GONE
                    tvNext.visibility = View.VISIBLE
                    btnStartExercise.visibility = View.INVISIBLE
                    btnSubjectMatter.visibility = View.INVISIBLE
                }
            }
            1 -> {
                binding?.apply {
                    nav1.setBackgroundResource(R.drawable.bg_nav_active)
                    nav2.setBackgroundResource(R.drawable.bg_nav_active)
                    nav3.setBackgroundResource(R.drawable.bg_nav_nonactive)
                    tvBack.visibility = View.VISIBLE
                    tvNext.visibility = View.VISIBLE
                    btnStartExercise.visibility = View.INVISIBLE
                    btnSubjectMatter.visibility = View.INVISIBLE
                }
            }
            2 -> {
                binding?.apply {
                    nav1.setBackgroundResource(R.drawable.bg_nav_active)
                    nav2.setBackgroundResource(R.drawable.bg_nav_active)
                    nav3.setBackgroundResource(R.drawable.bg_nav_active)
                    tvBack.visibility = View.VISIBLE
                    tvNext.visibility = View.INVISIBLE
//                    playerView.visibility = View.VISIBLE
                    btnStartExercise.visibility = View.VISIBLE
                    btnSubjectMatter.visibility = View.VISIBLE
                }
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
}