package com.veselove.myapplicationexoplayertest220304

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var btnPlay: Button
    lateinit var btnNext: Button
    lateinit var btnPrev: Button
    lateinit var tvPlaybackPosition: TextView
    lateinit var sbPlaybackPosition: SeekBar
    lateinit var sbVolume: SeekBar

    lateinit var btnTest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnTest = findViewById(R.id.btnTest)
        tvPlaybackPosition = findViewById(R.id.tvTime)
        sbPlaybackPosition = findViewById(R.id.sbPlaybackPosition)
        sbVolume = findViewById(R.id.sbVolume)

        val intent = Intent(this, PlayerNotificationService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        btnPlay.setOnClickListener {
            startService(Intent(this, PlayerNotificationService::class.java).apply {
                putExtra(PlayerNotificationService.PLAY_PAUSE_ACTION, 0)
            })
        }

        btnNext.setOnClickListener {
            startService(Intent(this, PlayerNotificationService::class.java).apply {
                putExtra(PlayerNotificationService.PLAY_PAUSE_ACTION, 1)
            })
        }

        btnPrev.setOnClickListener {
            startService(Intent(this, PlayerNotificationService::class.java).apply {
                putExtra(PlayerNotificationService.PLAY_PAUSE_ACTION, 2)
            })
        }

        btnTest.setOnClickListener {
            startService(Intent(this, PlayerNotificationService::class.java).apply {
                putExtra(PlayerNotificationService.PLAY_PAUSE_ACTION, 3)
            })
        }


    }

}