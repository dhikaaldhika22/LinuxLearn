package com.myproject.app.linuxlearn.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.HandlerCompat
import androidx.core.view.updateLayoutParams
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
    private var subjectMatterId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailSubjectMatterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setToolbar(getString(R.string.subject_matter))
        getDetailSubjectMatter()
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

            fullScreen.setOnClickListener {
                val i = Intent(this@DetailSubjectMatterActivity, FullScreenVideoActivity::class.java)
                i.putExtra(FullScreenVideoActivity.VIDEO_KEY, subjectMatterId)
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
        binding?.flVideo?.visibility = View.VISIBLE
        binding?.videoView?.visibility = View.VISIBLE
        val videoView = findViewById<VideoView>(R.id.videoView)
        val mediaController = MediaController(this@DetailSubjectMatterActivity)
        mediaController.setAnchorView(videoView)
        videoView?.setMediaController(mediaController)

        val videoUri = when (subjectMatterId) {
            "1" -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.dummy)
            }
            "2" -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.tes)
            }
            "3" -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.dummy)
            }
            else -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.tes)
            }
        }
        videoView?.setVideoURI(videoUri)
        videoView?.start()
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
                        subjectMatterId = intent.extras?.getString("id")
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
                                    getDetailInfo()

                                    shimmerSubjectName.visibility = View.GONE
                                    shimmerContent.visibility = View.GONE

                                    tvNext.visibility = View.VISIBLE
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
        binding?.apply {
            flVideo.visibility = View.VISIBLE
            videoView.visibility = View.VISIBLE
        }
        val videoView = findViewById<VideoView>(R.id.videoView)
        val mediaController = MediaController(this@DetailSubjectMatterActivity)
        mediaController.setAnchorView(videoView)
        videoView?.setMediaController(mediaController)

        val videoUri = when (subjectMatterId) {
            "1" -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.dummy)
            }
            "2" -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.nartoh9)
            }
            "3" -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.only)
            }
            else -> {
                Uri.parse("android.resource://" + packageName + "/" + R.raw.perhapslove)
            }
        }

        videoView?.setVideoURI(videoUri)

        when(currentSubjectMatterPosition) {
            0 -> {
                binding?.apply {
                    nav1.setBackgroundResource(R.drawable.bg_nav_active)
                    nav2.setBackgroundResource(R.drawable.bg_nav_nonactive)
                    nav3.setBackgroundResource(R.drawable.bg_nav_nonactive)
                    tvBack.visibility = View.GONE
                    tvNext.visibility = View.VISIBLE
                    flVideo.visibility = View.GONE
                    videoView.stopPlayback()
                    fullScreen.visibility = View.INVISIBLE
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
                    flVideo.visibility = View.GONE
                    videoView.stopPlayback()
                    fullScreen.visibility = View.INVISIBLE
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

                    videoView?.start()
                    tvBack.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        topToBottom = R.id.fl_video
                    }
                    fullScreen.visibility = View.VISIBLE
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