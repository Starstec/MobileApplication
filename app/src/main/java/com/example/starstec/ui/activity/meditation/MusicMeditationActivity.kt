package com.example.starstec.ui.activity.meditation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.starstec.databinding.ActivityMusicMeditationBinding

class MusicMeditationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicMeditationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicMeditationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoItem =
            MediaItem.fromUri("https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4")
        val audioItem =
            MediaItem.fromUri("https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3")

        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            exoPlayer.setMediaItem(videoItem)
            exoPlayer.addMediaItem(audioItem)
            exoPlayer.prepare()
        }

        binding.playerView.player = player
    }
}