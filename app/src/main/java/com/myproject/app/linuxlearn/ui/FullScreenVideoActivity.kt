package com.myproject.app.linuxlearn.ui

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.MediaController
import androidx.annotation.RequiresApi
import com.myproject.app.linuxlearn.R
import com.myproject.app.linuxlearn.databinding.ActivityFullScreenVideoBinding

class FullScreenVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenVideoBinding

    private var subjectMatterId: String? = null



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())

        subjectMatterId = intent.extras?.getString(VIDEO_KEY)

        playVideo()
    }

    private fun playVideo() {
        binding.apply {
            val mediaController = MediaController(this@FullScreenVideoActivity)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)

            val videoUri = when (subjectMatterId) {
                "1" -> {
                    Uri.parse("android.resource://" + packageName + "/" + R.raw.dasarlinux)
                }
                "2" -> {
                    Uri.parse("android.resource://" + packageName + "/" + R.raw.operasifile)
                }
                "3" -> {
                    Uri.parse("android.resource://" + packageName + "/" + R.raw.manajemenproses)
                }
                else -> {
                    Uri.parse("android.resource://" + packageName + "/" + R.raw.manajemenaplikasi)
                }
            }
            videoView.setVideoURI(videoUri)
            videoView.start()
        }
    }

    companion object {
        const val VIDEO_KEY = "video_key"
    }
}