package com.example.malortpowerplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_worker_home.*
import java.util.Timer
import kotlin.concurrent.schedule

class WorkerHomeActivity : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_home)

        var timerLabel = findViewById<TextView>(R.id.timerLabel)
        val timer = RadiationHandler.instance.setRadiationTimer()
        timer.start()
        updateTimerUI()
    }

    private fun warningNotification(hours: Int, minutes: Int, seconds: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("RADIATION WARNING")
        builder.setMessage("You need to leave the room in $hours hours, $minutes minutes and $seconds seconds!")
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun updateTimerUI() {
        countDownTimer =
            object : CountDownTimer(RadiationHandler.instance.calculateSafetyTime() * 1000L, 1000) {
                override fun onFinish() {
                    Log.d("u ded", "u ded")
                }

                override fun onTick(millisUntilFinished: Long) {
                    val hours = RadiationHandler.instance.getHours()
                    val minutes = RadiationHandler.instance.getMinutes()
                    val seconds = RadiationHandler.instance.getSeconds()
                    timerLabel.text = "$hours:$minutes:$seconds"

                    if (hours == 2 && minutes == 53 && seconds == 30) {
                        warningNotification(hours, minutes, seconds)
                    }
                }
            }
        countDownTimer.start()
    }
}