package com.example.pm2e1grupo4

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class VideoActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        playerView = findViewById(R.id.playerView)

        val videoUrl = intent.getStringExtra("videoUrl")
        if (!videoUrl.isNullOrEmpty()) {
            // Usamos la URL tal cual viene de la API
            player = ExoPlayer.Builder(this).build().also { exoPlayer ->
                playerView.player = exoPlayer
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true

                // Listener de estado y errores
                exoPlayer.addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        Toast.makeText(
                            this@VideoActivity,
                            "Error al reproducir: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        if (isPlaying) {
                            Toast.makeText(
                                this@VideoActivity,
                                "Reproduciendo video",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_BUFFERING -> Toast.makeText(
                                this@VideoActivity,
                                "Cargando video...",
                                Toast.LENGTH_SHORT
                            ).show()
                            Player.STATE_READY -> Toast.makeText(
                                this@VideoActivity,
                                "Video listo para reproducir",
                                Toast.LENGTH_SHORT
                            ).show()
                            Player.STATE_ENDED -> Toast.makeText(
                                this@VideoActivity,
                                "Video terminado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } else {
            Toast.makeText(this, "No hay video disponible", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}
